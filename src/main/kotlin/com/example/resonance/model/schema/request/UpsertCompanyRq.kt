package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertCompanyRq (
    @field:NotBlank(message = "Не указано название компании")
    val companyName : String,

    @field:NotBlank(message = "Не указано описание")
    val companyDescription: String,

    @field:NotBlank(message = "Не указана индустрия")
    val industry: String,

    @field:NotBlank(message = "Не указана ссылка на сайт")
    val websiteLink : String,

    val careerPageLink : String?,
)