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
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RankRedisService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) : RankService {

    init {
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
    }

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

    private fun convertToJson(memberRank: Any): String =
        try {
            objectMapper.writeValueAsString(memberRank)
        } catch (e: JsonProcessingException) {
            throw RankAccessException.parseJson(e)
        }

    private fun <T> convertFromJson(json: String?, type: Class<T>): T =
        try {
            objectMapper.readValue(json, type)
        } catch (e: JsonProcessingException) {
            throw RankAccessException.parseJson(e)
        }

    private fun updateRank(target: String, zSetMember: String, addPoint: Int) {
        redisTemplate.opsForZSet().incrementScore(target, zSetMember, addPoint.toDouble())
    }

    override fun getMemberRank(page: Long, size: Long): List<MemberRankResponse> =
        getMemberRank(MEMBER_RANK_KEY, page, size)

    override fun getOrganizationRank(
        organizationType: OrganizationType,
        page: Long,
        size: Long
    ): List<OrganizationRankResponse> =
        getOrganizationRank("${ORGANIZATION_TYPE_RANK_KEY}${organizationType.name}", page, size)

    override fun getAllOrganizationRank(page: Long, size: Long): List<OrganizationRankResponse> =
        getOrganizationRank(ORGANIZATION_RANK_KEY, page, size)

    override fun getOrganizationMemberRank(organizationId: Long, page: Long, size: Long): List<MemberRankResponse> =
        getMemberRank("${ORGANIZATION_MEMBER_RANK_KEY}$organizationId", page, size)

    private fun getOrganizationRank(target: String, page: Long, size: Long): List<OrganizationRankResponse> = try {
        val start = page * size
        val end = (page + 1) * size - 1
        val rank = redisTemplate.opsForZSet().reverseRangeWithScores(target, start, end)
        rank?.map {
            getOrganizationDetails(it.value.toString().toLong(), it.score?.toLong() ?: 0L)
        } ?: emptyList()
    } catch (e: Exception) {
        throw RankAccessException.get(e)
    }

    private fun getMemberRank(target: String, page: Long, size: Long): List<MemberRankResponse> = try {
        val start = page * size
        val end = (page + 1) * size - 1
        val rank = redisTemplate.opsForZSet().reverseRangeWithScores(target, start, end)
        rank?.map {
            getMemberDetails(it.value.toString(), it.score?.toLong() ?: 0L)
        } ?: emptyList()
    } catch (e: Exception) {
        throw RankAccessException.get(e)
    }

    override fun getMemberProfileRank(member: Member): ProfileRank = try {
        val totalMemberNum = getTotalMemberNum(member)
        val rank = getRankByMember(member) ?: 0L
        val organizationRank = getOrganizationRankByMember(member) ?: 0L
        if (totalMemberNum <= 3L) {
            val githubIds = findAllOrganizationMembers(member, totalMemberNum)
            ProfileRank(githubIds, rank.toInt(), organizationRank.toInt(), totalMemberNum == organizationRank + 1L)
        } else {
            val adjacentRanks = calculateAdjacentRanks(organizationRank, totalMemberNum)
            redisTemplate
                .opsForZSet()
                .range("${ORGANIZATION_MEMBER_RANK_KEY}${member.githubId}", adjacentRanks[0], adjacentRanks[1])
                ?.let { githubIds ->
                    ProfileRank(
                        githubIds.map { it.toString() },
                        rank.toInt(),
                        organizationRank.toInt(),
                        totalMemberNum == organizationRank + 1L
                    )
                } ?: ProfileRank(
                emptyList(),
                rank.toInt(),
                organizationRank.toInt(),
                totalMemberNum == organizationRank + 1L
            )
        }
    } catch (e: Exception) {
        throw RankAccessException.get(e)
    }

    override fun getMemberRankValue(member: Member): Int {
        return getRankByMember(member)?.toInt() ?: 0
    }

    private fun getTotalMemberNum(member: Member) =
        redisTemplate.opsForZSet().zCard("${ORGANIZATION_MEMBER_RANK_KEY}${member.organization?.id}") ?: 0L

    private fun getRankByMember(member: Member) =
        redisTemplate.opsForZSet().rank(MEMBER_RANK_KEY, member.githubId)

    private fun getOrganizationRankByMember(member: Member) =
        redisTemplate.opsForZSet().rank("${ORGANIZATION_MEMBER_RANK_KEY}${member.organization?.id}", member.githubId)

    private fun calculateAdjacentRanks(organizationRank: Long, totalMemberNum: Long) =
        when {
            organizationRank == 0L -> longArrayOf(0L, 3L)
            organizationRank == totalMemberNum - 1L -> longArrayOf(organizationRank - 3L, totalMemberNum)
            else -> longArrayOf(organizationRank - 1L, organizationRank + 1L)
        }

    private fun findAllOrganizationMembers(member: Member, totalMemberNum: Long): List<String> =
        redisTemplate.opsForZSet().range("${ORGANIZATION_MEMBER_RANK_KEY}${member.organization?.id}", 0, totalMemberNum)
            ?.toList() ?: emptyList()

    private fun getOrganizationDetails(id: Long, score: Long): OrganizationRankResponse {
        val json = redisTemplate.opsForValue().get(getMemberDetailsKey(id.toString()))
        val response = convertFromJson(json, OrganizationRankResponse::class.java)
        return response.apply { this.contributionAmount = score }
    }

    private fun getMemberDetails(githubId: String, score: Long): MemberRankResponse {
        val json = redisTemplate.opsForValue().get(getMemberDetailsKey(githubId))
        val response = convertFromJson(json, MemberRankResponse::class.java)
        return response.apply { this.contributionAmount = score }
    }

    companion object {
        private const val MEMBER_RANK_KEY = "rank:member"
        private const val ORGANIZATION_MEMBER_RANK_KEY = "rank:organization_member:"
        private const val ORGANIZATION_TYPE_RANK_KEY = "rank:organization_type:"
        private const val ORGANIZATION_RANK_KEY = "rank:organization"
        private const val MEMBER_DETAILS = "rank:member_details:"
    }
}
