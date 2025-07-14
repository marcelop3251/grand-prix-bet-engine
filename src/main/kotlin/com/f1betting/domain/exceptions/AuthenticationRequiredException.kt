package com.f1betting.domain.exceptions

class AuthenticationRequiredException(
    message: String = "Authentication required",
    cause: Throwable? = null
) : BusinessException(
    type = "AUTHENTICATION_REQUIRED",
    message = message,
    cause = cause
) 