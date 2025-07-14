package com.f1betting.interfaces.rest.exception

import com.f1betting.domain.exceptions.AuthenticationRequiredException
import com.f1betting.domain.exceptions.BusinessException
import com.f1betting.domain.exceptions.DriverNotAvailableException
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.exceptions.EventAlreadyFinishedException
import com.f1betting.domain.exceptions.InsufficientBalanceException
import com.f1betting.domain.exceptions.InvalidFilterParameterException
import com.f1betting.domain.exceptions.OutcomeAlreadyExistsException
import java.time.LocalDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val status = when (ex) {
            is EntityNotFoundException -> HttpStatus.NOT_FOUND
            is InsufficientBalanceException -> HttpStatus.BAD_REQUEST
            is DriverNotAvailableException -> HttpStatus.BAD_REQUEST
            is OutcomeAlreadyExistsException -> HttpStatus.CONFLICT
            is EventAlreadyFinishedException -> HttpStatus.BAD_REQUEST
            is InvalidFilterParameterException -> HttpStatus.BAD_REQUEST
            is AuthenticationRequiredException -> HttpStatus.UNAUTHORIZED
            else -> HttpStatus.BAD_REQUEST
        }
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message ?: "Business error",
            type = ex.type,
            path = request.getDescription(false)
        )
        return ResponseEntity(errorResponse, status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val fieldErrors = ex.bindingResult.fieldErrors
        val errorMessage = if (fieldErrors.isNotEmpty()) {
            val fieldError = fieldErrors.first()
            "Invalid field '${fieldError.field}': ${fieldError.defaultMessage}"
        } else {
            "Validation failed"
        }
        
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = errorMessage,
            type = null,
            path = request.getDescription(false)
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val parameterName = ex.name
        val parameterValue = ex.value
        val requiredType = ex.requiredType?.simpleName ?: "unknown"
        
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = "Invalid parameter '$parameterName' with value '$parameterValue'. Expected type: $requiredType",
            type = null,
            path = request.getDescription(false)
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid argument",
            type = null,
            path = request.getDescription(false)
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        println(ex.message)
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "An unexpected error occurred",
            type = null,
            path = request.getDescription(false)
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val type: String?,
    val path: String
) 