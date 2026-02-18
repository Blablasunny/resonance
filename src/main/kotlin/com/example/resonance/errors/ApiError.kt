package com.example.resonance.errors

import com.example.resonance.logger.ResonanceLogger
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@JsonIgnoreProperties("localizedMessage", "cause", "stackTrace", "suppressed")
open class ApiError(
    override val message: String,
) : Exception() {
    var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

    @JsonInclude(JsonInclude.Include.NON_EMPTY)

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]")
    val timestamp: LocalDateTime = LocalDateTime.now()

    private val logger = ResonanceLogger(this::class.java)

    constructor(
        status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        message: String,
    ) : this(message) {
        this.status = status
    }

    constructor(
        status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        message: String,
        exception: Throwable?,
    ) : this(status, message) {
        logger.error("Error with Status code \"${status.value()}:\n ${exception?.stackTraceToString()}")
    }
}