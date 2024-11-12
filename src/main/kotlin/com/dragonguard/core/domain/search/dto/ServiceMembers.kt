package com.dragonguard.core.domain.search.dto

class ServiceMembers(
    serviceMember: List<String>,
) {
    val serviceMember: MutableSet<String> = mutableSetOf()

    fun isServiceMember(name: String): Boolean = serviceMember.contains(name)

    init {
        this.serviceMember.addAll(serviceMember)
    }
}
