package com.dragonguard.core.domain.contribution

import com.dragonguard.core.global.audit.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
    @Column(nullable = false)
    val memberId: Long,
) : BaseEntity()
