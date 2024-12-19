package com.dragonguard.core.domain.contribution

import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany

@Embeddable
class Contributions {
    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val contributions: MutableList<Contribution> = mutableListOf()

    fun total(): Int = contributions.sumOf { it.amount }

    fun addAll(contributions: List<Contribution>): Int {
        var count = 0
        contributions
            .forEach {
                val value = it.amount - numOfType(it.contributionType)
                if (value > 0) {
                    it.amount = value
                    this.contributions.add(it)
                    count += value
                }
            }
        return count
    }

    fun numOfType(type: ContributionType): Int =
        contributions.filter { it.contributionType == type }.sumOf { it.amount }
}
