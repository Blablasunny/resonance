package com.example.resonance.errors

import org.springframework.http.HttpStatus

class PasswordsDontMatchesException: ApiError(
    status = HttpStatus.UNSUPPORTED_MEDIA_TYPE,
    message = "Подтверждение пароля не соответствует паролю",
)