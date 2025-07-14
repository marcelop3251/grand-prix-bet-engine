package com.f1betting.builders

import com.f1betting.domain.entities.Driver
import java.time.LocalDateTime

class DriverBuilder {
    
    var id: String = "driver1"
    var externalId: String = "d1"
    var driverNumber: Int = 1
    var firstName: String = "Max"
    var lastName: String = "Verstappen"
    var fullName: String = "Max Verstappen"
    var broadcastName: String? = null
    var nameAcronym: String? = null
    var countryCode: String? = null
    var teamName: String? = null
    var teamColour: String? = null
    var headshotUrl: String? = null
    var createdAt: LocalDateTime = LocalDateTime.now()
    var updatedAt: LocalDateTime = LocalDateTime.now()
    
    fun build() = Driver(
        id = this.id,
        externalId = this.externalId,
        driverNumber = this.driverNumber,
        firstName = this.firstName,
        lastName = this.lastName,
        fullName = this.fullName,
        broadcastName = this.broadcastName,
        nameAcronym = this.nameAcronym,
        countryCode = this.countryCode,
        teamName = this.teamName,
        teamColour = this.teamColour,
        headshotUrl = this.headshotUrl,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
    
    companion object {
        fun build(block: (DriverBuilder.() -> Unit)? = null) = when (block) {
            null -> DriverBuilder().build()
            else -> DriverBuilder().apply(block).build()
        }
    }
} 