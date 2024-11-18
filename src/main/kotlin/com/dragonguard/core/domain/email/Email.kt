package com.dragonguard.core.domain.email

import com.dragonguard.core.global.audit.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.util.concurrent.ThreadLocalRandom

@Entity
class Email(
    @Column(nullable = false)
    var organizationId: Long,
    @Column(nullable = false)
    var email: String,
) : BaseEntity() {

    var code: Int = ThreadLocalRandom.current().nextInt(MIN, MAX)
        protected set

    fun notMatches(code: Int): Boolean {
        return this.code == code
    }

    companion object {
        private const val MIN = 10000
        private const val MAX = 99999
    }
}
