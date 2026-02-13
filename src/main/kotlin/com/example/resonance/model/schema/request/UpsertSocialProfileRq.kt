package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertSocialProfileRq(
    @field:NotBlank(message = "Name is required")
    val platformName: String,
    @field:NotBlank(message = "Link is required")
    val platformLink: String,
)
