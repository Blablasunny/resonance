package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertSubjectRq(
    @field:NotBlank(message = "Name is required")
    val subjectName: String,
)
