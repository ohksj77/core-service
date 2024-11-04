package com.dragonguard.core.domain.contribution

import org.springframework.data.jpa.repository.JpaRepository

interface ContributionRepository : JpaRepository<Contribution, Long>
