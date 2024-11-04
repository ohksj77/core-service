package com.dragonguard.core.config.executor

import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading
import org.springframework.boot.autoconfigure.thread.Threading
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class ExecutorServiceConfig {
    @Bean
    @ConditionalOnThreading(Threading.VIRTUAL)
    fun virtualThreadExecutor(): ExecutorService = Executors.newVirtualThreadPerTaskExecutor()
}
