package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoInternalRequest

interface GitRepoMemberProducer {
    fun produce(request: GitRepoInternalRequest)
}
