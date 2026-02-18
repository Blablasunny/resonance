package com.example.resonance.errors

import org.springframework.http.HttpStatus

class DoesntHaveException(
    ownerType: String,
    entityType: String,
    dataOwner: Any? = null,
    dataEntity: Any? = null,
): ApiError(
    status = HttpStatus.NOT_FOUND,
    message = "$ownerType ${dataOwner ?: ""} не имеет $entityType ${dataEntity ?: ""}",
)