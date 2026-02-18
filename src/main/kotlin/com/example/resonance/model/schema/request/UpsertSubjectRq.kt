package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertSubjectRq(
    @field:NotBlank(message = "Не указано название предмета")
    val subjectName: String,
)
