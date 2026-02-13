package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertCompanyRq (
    @field:NotBlank(message = "Name is required")
    val companyName : String,
    @field:NotBlank(message = "Description is required")
    val companyDescription: String,
    @field:NotBlank(message = "Industry is required")
    val industry: String,
    @field:NotBlank(message = "Website link is required")
    val websiteLink : String,
    @field:NotBlank(message = "Career page ling is required")
    val careerPageLink : String,
)