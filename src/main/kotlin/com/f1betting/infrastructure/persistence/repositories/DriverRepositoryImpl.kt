package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.domain.entities.Driver
import com.f1betting.domain.repositories.DriverRepository
import com.f1betting.infrastructure.persistence.entities.DriverEntity
import com.f1betting.infrastructure.persistence.repositories.spring.DriverJpaRepository
import org.springframework.stereotype.Component

@Component
class DriverRepositoryImpl(
    private val driverJpaRepository: DriverJpaRepository
) : DriverRepository {
    override fun findByExternalId(externalId: String): Driver? =
        driverJpaRepository.findByExternalId(externalId)?.toDomain()

    override fun save(driver: Driver): Driver =
        driverJpaRepository.save(DriverEntity.fromDomain(driver)).toDomain()

    override fun findById(id: String): Driver? =
        driverJpaRepository.findById(id).orElse(null)?.toDomain()
} 