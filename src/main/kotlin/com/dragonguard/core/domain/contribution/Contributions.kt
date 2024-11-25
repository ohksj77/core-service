package com.dragonguard.core.domain.contribution

import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany

@Embeddable
class Contributions {
    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val contributions: MutableList<Contribution> = mutableListOf()

    fun total(): Int = contributions.sumOf { it.amount }

    fun addAll(contributions: List<Contribution>) {
        contributions.map {
            val calculatedAmount = it.amount - numOfType(it.contributionType)
            if (calculatedAmount > 0) {
                this.contributions.add(Contribution(it.contributionType, calculatedAmount, it.year, it.member))
            }
        }
    }

    fun numOfType(type: ContributionType): Int =
        contributions.filter { it.contributionType == type }.sumOf { it.amount }
}
