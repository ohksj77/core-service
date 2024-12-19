package com.dragonguard.core.domain.gitorg

import com.dragonguard.core.domain.gitorg.dto.GitOrgResponse
import org.springframework.stereotype.Component

@Component
class GitOrgMapper {

    fun toResponses(gitOrgs: List<GitOrg>): List<GitOrgResponse> {
        return gitOrgs.map { toResponse(it) }
    }

    private fun toResponse(gitOrg: GitOrg): GitOrgResponse {
        return GitOrgResponse(
            gitOrg.name,
            gitOrg.profileImage,
        )
    }
}