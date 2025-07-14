package com.f1betting.infrastructure.config

import io.netty.channel.ChannelOption
import java.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider

@Configuration
class WebClientConfig {
    
    @Bean
    fun openF1WebClient(
        @Value("\${openf1.base-url}") baseUrl: String,
    ): WebClient {
        
        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(buildHttpClient("openf1", 100))
            .build()
    }

    private fun buildHttpClient(name: String, maxConnection: Int = 30): ReactorClientHttpConnector {
        return ReactorClientHttpConnector(
            HttpClient
                .create(
                    ConnectionProvider.builder(name)
                    .maxConnections(maxConnection)
                    .pendingAcquireTimeout(Duration.ofSeconds(2))
                    .maxIdleTime(Duration.ofSeconds(30))
                    .build())
                .compress(true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
        )
    }
} 