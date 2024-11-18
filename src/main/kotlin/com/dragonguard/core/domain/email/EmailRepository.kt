package com.dragonguard.core.domain.email

import org.springframework.data.jpa.repository.JpaRepository

interface EmailRepository : JpaRepository<Email, Long>
