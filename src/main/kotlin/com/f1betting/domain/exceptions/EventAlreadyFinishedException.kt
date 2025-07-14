package com.f1betting.domain.exceptions

class EventAlreadyFinishedException(
    eventId: String,
    cause: Throwable? = null
) : BusinessException(
    type = "EVENT_ALREADY_FINISHED",
    message = "Event $eventId already has an outcome and cannot accept new bets",
    cause = cause
) 