package com.dragonguard.core.domain.contribution

import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany

@Embeddable
class Contributions {
    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val contributions: MutableList<Contribution> = mutableListOf()

    fun total(): Int = contributions.sumOf { it.amount }

    fun addAll(contributions: List<Contribution>): Int = contributions.sumOf { contribution ->
        val value = (contribution.amount - numOfType(contribution.contributionType)).coerceAtLeast(0)
        if (value > 0) {
            contribution.amount = value
            this.contributions.add(contribution)
        }
        value
    }

    fun numOfType(type: ContributionType): Int =
        contributions.filter { it.contributionType == type }.sumOf { it.amount }
}
