package com.f1betting.domain.exceptions

abstract class BusinessException(
    val type: String,
    message: String,
    cause: Throwable? = null,
    enableSuppression: Boolean = false,
    writableStackTrace: Boolean = false
) : RuntimeException(message, cause, enableSuppression, writableStackTrace) 