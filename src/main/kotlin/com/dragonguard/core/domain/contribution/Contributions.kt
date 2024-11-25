package com.dragonguard.core.domain.contribution

import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany

@Embeddable
class Contributions {
    @OneToMany(mappedBy = "member")
    val contributions: MutableList<Contribution> = mutableListOf()

    fun total(): Int = contributions.sumOf { it.amount }

    fun addAll(contributions: List<Contribution>) {
        this.contributions.addAll(contributions)
    }

    fun numOfType(type: ContributionType): Int =
        contributions.filter { it.contributionType == type }.sumOf { it.amount }
}
