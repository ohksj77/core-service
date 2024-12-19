package com.dragonguard.core.domain.rank

import com.dragonguard.core.domain.contribution.dto.ContributionRequest
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.Organization
import com.dragonguard.core.domain.organization.OrganizationType
import com.dragonguard.core.domain.rank.dto.MemberRankResponse
import com.dragonguard.core.domain.rank.dto.OrganizationRankResponse
import com.dragonguard.core.domain.rank.dto.ProfileRank
import com.dragonguard.core.domain.rank.exception.RankAccessException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RankRedisService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) : RankService {
    override fun addContribution(
        contributionRequest: ContributionRequest,
        totalAmount: Int,
        member: Member,
    ) {
        try {
            updateRank(MEMBER_RANK_KEY, contributionRequest.githubId, totalAmount)
            contributionRequest.organizationId?.let {
                updateRank("${ORGANIZATION_MEMBER_RANK_KEY}${it}", contributionRequest.githubId, totalAmount)
                updateRank(
                    "${ORGANIZATION_TYPE_RANK_KEY}${contributionRequest.organizationType?.name}",
                    contributionRequest.githubId,
                    totalAmount
                )
                updateRank(ORGANIZATION_RANK_KEY, it.toString(), totalAmount)
                updateOrganizationDetails(member.organization)
            }
            updateMemberDetails(member)
        } catch (e: Exception) {
            throw RankAccessException.update(e)
        }
    }

    private fun updateOrganizationDetails(organization: Organization?) {
        redisTemplate
            .opsForValue()[getMemberDetailsKey(organization?.id.toString())] = convertToJson(
            OrganizationRankResponse(
                organization?.id,
                organization?.name,
                organization?.organizationType,
            )
        )
    }

    private fun updateMemberDetails(member: Member) {
        redisTemplate
            .opsForValue()[getMemberDetailsKey(member.githubId)] = convertToJson(
            MemberRankResponse(
                member.id,
                member.githubId,
                member.tier,
                member.profileImage,
            )
        )
    }

    private fun getMemberDetailsKey(githubId: String): String = MEMBER_DETAILS + githubId

    private fun convertToJson(memberRank: MemberRankResponse): String =
        try {
            objectMapper.writeValueAsString(memberRank)
        } catch (e: JsonProcessingException) {
            throw RankAccessException.parseJson(e)
        }

    private fun convertToJson(organizationRank: OrganizationRankResponse): String =
        try {
            objectMapper.writeValueAsString(organizationRank)
        } catch (e: JsonProcessingException) {
            throw RankAccessException.parseJson(e)
        }


    private fun updateRank(
        target: String,
        zSetMember: String,
        addPoint: Int,
    ) {
        redisTemplate.execute { connection ->
            connection.zSetCommands().zIncrBy(
                target.toByteArray(),
                addPoint.toDouble(),
                zSetMember.toByteArray(),
            )
        }
    }

    override fun getMemberRank(
        page: Long,
        size: Long,
    ): List<MemberRankResponse> = getMemberRank(MEMBER_RANK_KEY, page, size)

    override fun getOrganizationRank(
        organizationType: OrganizationType,
        page: Long,
        size: Long,
    ): List<OrganizationRankResponse> =
        getOrganizationRank("${ORGANIZATION_TYPE_RANK_KEY}${organizationType.name}", page, size)

    override fun getAllOrganizationRank(page: Long, size: Long): List<OrganizationRankResponse> =
        getOrganizationRank(ORGANIZATION_RANK_KEY, page, size)

    override fun getOrganizationMemberRank(
        organizationId: Long,
        page: Long,
        size: Long,
    ): List<MemberRankResponse> =
        getMemberRank("${ORGANIZATION_MEMBER_RANK_KEY}$organizationId", page, size)

    private fun getOrganizationRank(
        target: String,
        page: Long,
        size: Long,
    ) = try {
        val start = page * size
        val end = (page + 1) * size - 1
        val rank =
            redisTemplate.execute { connection ->
                connection.zSetCommands().zRevRangeWithScores(
                    target.toByteArray(),
                    start,
                    end,
                )
            }

        rank?.map {
            getOrganizationDetails(it.value.toString().toLong(), it.score.toLong())
        } ?: emptyList()
    } catch (e: Exception) {
        throw RankAccessException.get(e)
    }

    private fun getMemberRank(
        target: String,
        page: Long,
        size: Long,
    ) = try {
        val start = page * size
        val end = (page + 1) * size - 1
        val rank =
            redisTemplate.execute { connection ->
                connection.zSetCommands().zRevRangeWithScores(
                    target.toByteArray(),
                    start,
                    end,
                )
            }

        rank?.map {
            getMemberDetails(it.value.toString(), it.score.toLong())
        } ?: emptyList()
    } catch (e: Exception) {
        throw RankAccessException.get(e)
    }

    override fun getMemberProfileRank(member: Member): ProfileRank =
        try {
            val totalMemberNum =
                getTotalMemberNum(member)
            val rank = getRankByMember(member) ?: 0L
            val organizationRank = getOrganizationRankByMember(member) ?: 0L

            if (totalMemberNum <= 3L) {
                val githubIds = findAllOrganizationMembers(member, totalMemberNum)
                ProfileRank(githubIds, rank.toInt(), organizationRank.toInt(), totalMemberNum == organizationRank + 1L)
            } else {
                val adjacentRanks = calculateAdjacentRanks(organizationRank, totalMemberNum)

                redisTemplate
                    .execute { connection ->
                        connection
                            .zSetCommands()
                            .zRange(
                                "${ORGANIZATION_MEMBER_RANK_KEY}${member.githubId}".toByteArray(),
                                adjacentRanks[0],
                                adjacentRanks[1],
                            )
                    }.let {
                        it?.map { memberByte -> memberByte.toString() }?.let { githubIds ->
                            ProfileRank(
                                githubIds,
                                rank.toInt(),
                                organizationRank.toInt(),
                                totalMemberNum == organizationRank + 1L,
                            )
                        }
                    } ?: ProfileRank.empty()
            }
        } catch (e: Exception) {
            throw RankAccessException.get(e)
        }

    override fun getMemberRankValue(member: Member): Int =
        getRankByMember(member)?.toInt() ?: 0

    private fun calculateAdjacentRanks(
        it: Long,
        totalMemberNum: Long,
    ): List<Long> =
        when (it) {
            0L -> listOf(0L, 2L)
            totalMemberNum - 1L -> listOf(totalMemberNum - 3L, totalMemberNum - 1L)
            else -> listOf(it - 1L, it + 1L)
        }

    private fun getRankByMember(member: Member): Long? =
        redisTemplate.execute { connection ->
            connection.zSetCommands().zRank(
                MEMBER_RANK_KEY.toByteArray(),
                member.githubId.toByteArray(),
            )
        }?.plus(1L)

    private fun getOrganizationRankByMember(member: Member): Long? =
        member.organization?.let {
            redisTemplate.execute { connection ->
                connection.zSetCommands().zRank(
                    "${ORGANIZATION_MEMBER_RANK_KEY}${it.id}".toByteArray(),
                    member.githubId.toByteArray(),
                )
            }
        }?.plus(1L)

    private fun findAllOrganizationMembers(
        member: Member,
        totalMemberNum: Long,
    ): List<String> =
        member.organization?.let {
            redisTemplate
                .execute { connection ->
                    connection.zSetCommands().zRangeWithScores(
                        "${ORGANIZATION_MEMBER_RANK_KEY}${it.id}".toByteArray(),
                        0,
                        totalMemberNum - 1,
                    )
                }?.map {
                    it.value.toString()
                }
        } ?: emptyList()

    private fun getTotalMemberNum(member: Member): Long =
        member.organization?.let {
            redisTemplate.execute { connection ->
                connection.zSetCommands().zCard(
                    "${ORGANIZATION_MEMBER_RANK_KEY}${it.id}".toByteArray(),
                )
            }
        } ?: 0L

    private fun getMemberDetails(githubId: String, contributionAmount: Long): MemberRankResponse {
        val memberDetailsJson = redisTemplate.opsForValue().get(getMemberDetailsKey(githubId))
        val rankResponse = convertMemberDetailsFromJson(memberDetailsJson)
        rankResponse.contributionAmount = contributionAmount
        return rankResponse
    }

    private fun getOrganizationDetails(organizationId: Long, contributionAmount: Long): OrganizationRankResponse {
        val organizationDetailsJson =
            redisTemplate.opsForValue().get(getMemberDetailsKey(organizationId.toString()))
        val rankResponse = convertOrganizationFromJson(organizationDetailsJson)
        rankResponse.contributionAmount = contributionAmount
        return rankResponse
    }

    private fun convertOrganizationFromJson(organizationDetailsJson: String?): OrganizationRankResponse {
        return try {
            objectMapper.readValue(organizationDetailsJson, OrganizationRankResponse::class.java)
        } catch (e: Exception) {
            throw RankAccessException.parseJson(e)
        }
    }

    private fun convertMemberDetailsFromJson(memberDetailsJson: String?): MemberRankResponse {
        return try {
            objectMapper.readValue(memberDetailsJson, MemberRankResponse::class.java)
        } catch (e: Exception) {
            throw RankAccessException.parseJson(e)
        }
    }


    companion object {
        private const val MEMBER_RANK_KEY = "rank:member"
        private const val ORGANIZATION_RANK_KEY = "rank:organization"
        private const val ORGANIZATION_TYPE_RANK_KEY = "rank:organization:type:"
        private const val ORGANIZATION_MEMBER_RANK_KEY = "rank:organization:member:"
        private const val MEMBER_DETAILS = "member:details:"
    }
}
