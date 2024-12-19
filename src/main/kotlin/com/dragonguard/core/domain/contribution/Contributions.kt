package com.dragonguard.core.domain.contribution

import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany

@Embeddable
class Contributions {
    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val contributions: MutableList<Contribution> = mutableListOf()

    fun total(): Int = contributions.sumOf { it.amount }

    fun addAll(contributions: List<Contribution>): Boolean {
        val values = contributions.map { it.amount - numOfType(it.contributionType) }.filter { it > 0 }
        if (values.isNotEmpty()) {
            this.contributions.addAll(contributions)
            return true
        }
        return false
    }

    fun numOfType(type: ContributionType): Int =
        contributions.filter { it.contributionType == type }.sumOf { it.amount }
}
