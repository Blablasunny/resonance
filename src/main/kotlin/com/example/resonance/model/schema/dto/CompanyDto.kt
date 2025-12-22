package com.example.resonance.model.schema.dto

import java.time.LocalDateTime
import java.util.UUID

data class CompanyDto (
    val id : UUID?,
    val companyName : String,
    val companyDescription : String,
    val industry : String,
    val websiteLink : String,
    val careerPageLink : String,
    val createdAt : LocalDateTime,
    val updatedAt : LocalDateTime,
)