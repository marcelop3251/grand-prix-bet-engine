package com.f1betting.application.usecases

import com.f1betting.application.usecases.commands.ListEventsCommand
import com.f1betting.domain.entities.Event
import com.f1betting.domain.repositories.EventRepository
import com.f1betting.domain.specifications.EventFilterStrategyFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListEventsUseCase(
    private val eventRepository: EventRepository,
    private val filterStrategyFactory: EventFilterStrategyFactory
) {
    
    fun execute(command: ListEventsCommand): List<Event> {
        val specification = filterStrategyFactory.buildSpecification(command)
        return eventRepository.findAll(specification)
    }
} 