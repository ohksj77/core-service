package com.dragonguard.core.domain.organization

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.global.audit.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import org.hibernate.annotations.SoftDelete

@Entity
@SoftDelete
class Organization(
    @Column(nullable = false, unique = true)
    var name: String,
    var emailEndpoint: String,
    @Enumerated(EnumType.STRING)
    var organizationType: OrganizationType,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var organizationStatus: OrganizationStatus = OrganizationStatus.REQUESTED

    @OneToMany(fetch = FetchType.LAZY)
    var members: MutableList<Member> = mutableListOf()

    fun validateAndUpdateEmail(member: Member, email: String) {
        if (!email.endsWith(emailEndpoint)) {
            throw InvalidEmailException()
        }
        member.email = email
    }

    fun approve() {
        this.organizationStatus = OrganizationStatus.APPROVED
    }

    fun memberContributionAmount(): Int =
        members.sumOf { it.contributions.total() }
}
