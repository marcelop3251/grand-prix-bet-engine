package com.f1betting.domain.exceptions

class DriverNotAvailableException(
    driverId: String,
    eventId: String,
    cause: Throwable? = null
) : BusinessException(
    type = "DRIVER_NOT_AVAILABLE",
    message = "Driver $driverId is not available for event $eventId",
    cause = cause
) 