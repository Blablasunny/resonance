package com.example.resonance.errors

import org.springframework.boot.json.JsonParseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApiError::class)
    private fun handleApiError(ex: ApiError): ResponseEntity<ApiError> {
        return ResponseEntity.status(ex.status).body(ex)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val message = ex.bindingResult.fieldErrors.joinToString("; ") { error ->
            "${error.field}: ${error.defaultMessage}"
        }
        val apiError = ApiError(
            status = HttpStatus.BAD_REQUEST,
            message = "Validation failed: $message",
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonParseException(ex: HttpMessageNotReadableException): ResponseEntity<ApiError> {

        val message = if (ex.cause is JsonParseException) {
            "Invalid JSON format: ${ex.cause?.message}"
        } else {
            "Request body is not valid"
        }

        val apiError = ApiError(
            status = HttpStatus.BAD_REQUEST,
            message = message,
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiError> {

        val apiError = ApiError(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = "Internal server error",
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError)
    }
}