package com.example.resonance.errors

import org.springframework.http.HttpStatus

class NotFountException(
    entityType: String,
    data: Any? = null,
): ApiError(
    status = HttpStatus.NOT_FOUND,
    message = "$entityType ${data ?: ""} не существует",
)