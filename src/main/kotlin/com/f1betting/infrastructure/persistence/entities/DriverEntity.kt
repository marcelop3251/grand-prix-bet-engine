package com.f1betting.infrastructure.persistence.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import com.f1betting.domain.entities.Driver
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType

@Entity
@Table(name = "drivers")
data class DriverEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    val externalId: String,
    val driverNumber: Int,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val broadcastName: String? = null,
    val nameAcronym: String? = null,
    val countryCode: String? = null,
    val teamName: String? = null,
    val teamColour: String? = null,
    val headshotUrl: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): Driver = Driver(
        id = id,
        externalId = externalId,
        driverNumber = driverNumber,
        firstName = firstName,
        lastName = lastName,
        fullName = fullName,
        broadcastName = broadcastName,
        nameAcronym = nameAcronym,
        countryCode = countryCode,
        teamName = teamName,
        teamColour = teamColour,
        headshotUrl = headshotUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(driver: Driver): DriverEntity = DriverEntity(
            id = driver.id,
            externalId = driver.externalId,
            driverNumber = driver.driverNumber,
            firstName = driver.firstName,
            lastName = driver.lastName,
            fullName = driver.fullName,
            broadcastName = driver.broadcastName,
            nameAcronym = driver.nameAcronym,
            countryCode = driver.countryCode,
            teamName = driver.teamName,
            teamColour = driver.teamColour,
            headshotUrl = driver.headshotUrl,
            createdAt = driver.createdAt,
            updatedAt = driver.updatedAt
        )
    }
} 