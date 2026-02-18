package com.example.resonance.errors

import com.example.resonance.database.entity.UserType
import org.springframework.http.HttpStatus

class DataRequiredException(
    userType: UserType,
): ApiError(
    status = HttpStatus.BAD_REQUEST,
    message = "Требуются данные для $userType",
)