package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.global.audit.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.SoftDelete

@Entity
@SoftDelete
class Contribution(
    @Enumerated(EnumType.STRING)
    val contributionType: ContributionType,
    @Column(nullable = false)
    var amount: Int,
    @Column(nullable = false)
    val year: Int,
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val member: Member,
) : BaseEntity()
