package com.dragonguard.core.config.async

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.core.task.support.TaskExecutorAdapter
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.ExecutorService

@EnableAsync
@Configuration
class AsyncConfig : AsyncConfigurer {
    @Bean
    fun virtualAsyncTaskExecutor(
        @Qualifier("virtualThreadExecutor") virtualThreadExecutor: ExecutorService,
    ): AsyncTaskExecutor = TaskExecutorAdapter(virtualThreadExecutor)
}
