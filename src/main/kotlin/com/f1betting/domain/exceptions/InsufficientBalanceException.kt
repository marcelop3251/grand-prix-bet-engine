package com.f1betting.domain.exceptions

class InsufficientBalanceException(
    message: String = "Insufficient balance",
    cause: Throwable? = null
) : BusinessException(
    type = "INSUFFICIENT_BALANCE",
    message = message,
    cause = cause
) 