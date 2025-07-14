package com.f1betting.interfaces.rest.dto

import com.f1betting.domain.entities.Driver

data class DriverDto(
    val id: String?,
    val externalId: String,
    val driverNumber: Int,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val broadcastName: String?,
    val nameAcronym: String?,
    val countryCode: String?,
    val teamName: String?,
    val teamColour: String?,
    val headshotUrl: String?,
    val displayName: String
) {
    companion object {
        fun fromEntity(driver: Driver): DriverDto {
            return DriverDto(
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
                displayName = driver.getDisplayName()
            )
        }
    }
} 