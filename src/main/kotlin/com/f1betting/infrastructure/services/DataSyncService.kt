package com.f1betting.infrastructure.services

import com.f1betting.domain.entities.*
import com.f1betting.domain.repositories.*
import com.f1betting.infrastructure.gateway.openf1.OpenF1Client
import com.f1betting.infrastructure.gateway.openf1.dto.OpenF1DriverDto
import com.f1betting.infrastructure.gateway.openf1.dto.OpenF1SessionDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class DataSyncService(
    private val openF1Client: OpenF1Client,
    private val eventRepository: EventRepository,
    private val driverRepository: DriverRepository,
    private val eventDriverRepository: EventDriverRepository
) {
    
    private val oddsValues = listOf(BigDecimal("2.0"), BigDecimal("3.0"), BigDecimal("4.0"))
    
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    @Transactional
    suspend fun syncEvents() {
        val currentYear = java.time.LocalDateTime.now().year
        
        // Sync current year events
        val sessions = openF1Client.getSessions(year = currentYear)
        
        sessions.forEach { sessionDto ->
            val externalId = "${sessionDto.sessionKey}_${sessionDto.meetingKey}"
            
            if (!eventRepository.existsByExternalId(externalId)) {
                val event = Event(
                    externalId = externalId,
                    meetingKey = sessionDto.meetingKey,
                    sessionKey = sessionDto.sessionKey,
                    year = sessionDto.year,
                    countryName = sessionDto.countryName,
                    circuitShortName = sessionDto.circuitShortName,
                    sessionName = sessionDto.sessionName,
                    sessionType = sessionDto.sessionType,
                    dateStart = sessionDto.dateStart.toLocalDateTime(),
                    dateEnd = sessionDto.dateEnd?.toLocalDateTime(),
                    location = sessionDto.location
                )
                
                val savedEvent = eventRepository.save(event)
                
                // Sync drivers for this event
                syncDriversForEvent(savedEvent, sessionDto.sessionKey)
            }
        }
    }
    
    private suspend fun syncDriversForEvent(event: Event, sessionKey: Int) {
        val drivers = openF1Client.getDrivers(sessionKey)
        
        drivers.forEach { driverDto ->
            val driverExternalId = "${driverDto.driverNumber}_${sessionKey}"
            
            // Find or create driver
            val driver = driverRepository.findByExternalId(driverExternalId) ?: run {
                val newDriver = Driver(
                    externalId = driverExternalId,
                    driverNumber = driverDto.driverNumber,
                    firstName = driverDto.firstName,
                    lastName = driverDto.lastName,
                    fullName = driverDto.fullName,
                    broadcastName = driverDto.broadcastName,
                    nameAcronym = driverDto.nameAcronym,
                    countryCode = driverDto.countryCode,
                    teamName = driverDto.teamName,
                    teamColour = driverDto.teamColour,
                    headshotUrl = driverDto.headshotUrl
                )
                driverRepository.save(newDriver)
            }
            
            // Create event driver relationship with random odds
            if (!eventDriverRepository.existsByEventIdAndDriverId(event.id!!, driver.id!!)) {
                val randomOdds = oddsValues.random()
                val eventDriver = EventDriver(
                    event = event,
                    driver = driver,
                    odds = randomOdds
                )
                eventDriverRepository.save(eventDriver)
            }
        }
    }
} 