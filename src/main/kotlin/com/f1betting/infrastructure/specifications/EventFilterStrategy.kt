package com.f1betting.infrastructure.specifications

import com.f1betting.application.usecases.commands.ListEventsCommand
import com.f1betting.domain.entities.Event
import com.f1betting.domain.specifications.EventFilterStrategy
import com.f1betting.domain.specifications.EventFilterStrategyFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component

@Component
class EventFilterStrategyFactoryImpl(
    private val strategies: List<EventFilterStrategy>
) : EventFilterStrategyFactory {
    
    override fun buildSpecification(command: ListEventsCommand): Specification<Event> {
        val applicableStrategies = strategies.filter { it.canApply(command) }
        
        return if (applicableStrategies.isEmpty()) {
            Specification { _, _, _ -> null }
        } else {
            applicableStrategies
                .map { it.buildSpecification(command) }
                .reduce { acc, spec -> acc.and(spec) }
        }
    }
} 