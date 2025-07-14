package com.f1betting.domain.exceptions

class OutcomeAlreadyExistsException(
    eventId: String,
    cause: Throwable? = null
) : BusinessException(
    type = "OUTCOME_ALREADY_EXISTS",
    message = "Outcome already exists for event: $eventId",
    cause = cause
) 