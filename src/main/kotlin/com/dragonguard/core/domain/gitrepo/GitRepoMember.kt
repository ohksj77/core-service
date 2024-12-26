package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.global.audit.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.SoftDelete

@Entity
@SoftDelete
@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["git_repo_id", "member_id"])
    ]
)
class GitRepoMember(
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var gitRepo: GitRepo,
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var member: Member
) : BaseEntity() {
    @Embedded
    var gitRepoContribution: GitRepoContribution? = null
        protected set

    fun isSameMember(member: Member): Boolean = this.member.id == member.id
}
