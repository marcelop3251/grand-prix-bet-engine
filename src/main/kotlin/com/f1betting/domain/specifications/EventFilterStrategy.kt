package com.f1betting.domain.specifications

import com.f1betting.application.usecases.commands.ListEventsCommand
import com.f1betting.domain.entities.Event
import org.springframework.data.jpa.domain.Specification

interface EventFilterStrategy {
    fun canApply(command: ListEventsCommand): Boolean
    fun buildSpecification(command: ListEventsCommand): Specification<Event>
} 