package com.f1betting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class F1BettingApplication

fun main(args: Array<String>) {
    runApplication<F1BettingApplication>(*args)
} 