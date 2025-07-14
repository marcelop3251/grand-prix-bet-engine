package com.f1betting.domain.exceptions

class EntityNotFoundException(
    entityType: String,
    entityId: String,
    cause: Throwable? = null
) : BusinessException(
    type = "ENTITY_NOT_FOUND",
    message = "$entityType not found with id: $entityId",
    cause = cause
) 