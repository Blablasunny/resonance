package com.example.resonance.model.schema.request

data class UpsertCompanyRq (
    val companyName : String,
    val companyDescription: String,
    val industry: String,
    val websiteLink : String,
    val careerPageLink : String,
)