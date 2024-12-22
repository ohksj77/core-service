package com.dragonguard.core.config.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient
import java.net.http.HttpClient
import java.util.concurrent.Executors

@Configuration
class InternalRestClientConfig {

    @Bean("internalRestClient")
    fun restClient(
        @Value("\${internal-api.url}") url: String,
    ): RestClient =
        RestClient
            .builder()
            .baseUrl(url)
            .requestFactory(getClientHttpRequestFactory())
            .messageConverters { converters ->
                converters.add(MappingJackson2HttpMessageConverter(objectMapper()))
            }.build()

    private fun getClientHttpRequestFactory(): ClientHttpRequestFactory {
        val requestFactory =
            JdkClientHttpRequestFactory(
                HttpClient
                    .newBuilder()
                    .executor(Executors.newVirtualThreadPerTaskExecutor())
                    .build(),
            )
        return requestFactory
    }

    private fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }
}
