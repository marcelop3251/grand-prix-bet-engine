package com.f1betting.infrastructure.specifications

import com.f1betting.application.usecases.commands.ListEventsCommand
import com.f1betting.domain.entities.Event
import com.f1betting.domain.specifications.EventFilterStrategy
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component

@Component
class CountryFilterStrategy : EventFilterStrategy {
    override fun canApply(command: ListEventsCommand): Boolean {
        return command.country != null
    }
    
    override fun buildSpecification(command: ListEventsCommand): Specification<Event> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<String>("countryName"), command.country!!)
        }
    }
} 