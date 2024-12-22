package com.dragonguard.core.domain.member

import com.dragonguard.core.domain.contribution.Contribution
import com.dragonguard.core.domain.contribution.ContributionType
import com.dragonguard.core.domain.contribution.Contributions
import com.dragonguard.core.domain.organization.Organization
import com.dragonguard.core.global.audit.BaseEntity
import com.dragonguard.core.global.exception.NotInitializedException
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.SoftDelete
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Entity
@SoftDelete
class Member(
    @NaturalId
    @Column(nullable = false, unique = true)
    var githubId: String,
    @Column(length = 500)
    var profileImage: String,
) : BaseEntity() {
    @JoinColumn
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var organization: Organization? = null

    @Enumerated(EnumType.STRING)
    var authStep: AuthStep = AuthStep.NONE

    @CollectionTable
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private val _roles: MutableList<Role> = mutableListOf(Role.ROLE_USER)
    val roles: List<Role>
        get() = _roles.toList()

    @Enumerated(EnumType.STRING)
    var tier: Tier = Tier.SPROUT

    @Embedded
    var contributions: Contributions = Contributions()
        protected set

    var refreshToken: String? = null
    var githubToken: String? = null
        get() = field ?: throw NotInitializedException.memberGithubToken()
    var email: String? = null

    fun updateTier() {
        this.contributions.let {
            tier = Tier.fromPoint(it.total())
        }
    }

    fun addRole(role: Role) {
        _roles.add(role)
    }

    fun getHighestRole(): Role = roles.maxOrNull() ?: Role.ROLE_USER

    fun organize(organization: Organization) {
        this.organization = organization
    }

    fun getAuthorityByRoles(): List<SimpleGrantedAuthority> =
        roles
            .map(Role::name)
            .map(::SimpleGrantedAuthority)
            .toList()

    fun hasNoAuthStep(): Boolean = authStep == AuthStep.NONE

    fun updateGithubToken(githubToken: String) {
        this.githubToken = githubToken
    }

    fun join(
        profileImage: String,
    ) {
        this.authStep = AuthStep.GITHUB
        this.profileImage = profileImage
    }

    fun addContribution(contributions: List<Contribution>): Int {
        val updated = this.contributions.addAll(contributions)
        updateTier()
        return updated
    }

    fun contributionNumOfType(type: ContributionType): Int = this.contributions.numOfType(type)

    fun getTotalContribution(): Int = this.contributions.total()

    fun isLoginMember(): Boolean = authStep == AuthStep.EMAIL

    fun isServiceMember(): Boolean = authStep != AuthStep.NONE
}
