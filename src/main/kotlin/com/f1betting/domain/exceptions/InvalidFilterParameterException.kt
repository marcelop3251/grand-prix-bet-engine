package com.f1betting.domain.exceptions

class InvalidFilterParameterException(
    parameterName: String,
    value: String,
    cause: Throwable? = null
) : BusinessException(
    type = "INVALID_FILTER_PARAMETER",
    message = "Invalid filter parameter '$parameterName': '$value' cannot be empty or blank",
    cause = cause
) 