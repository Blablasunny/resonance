package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Company
import com.example.resonance.model.schema.request.UpsertCompanyRq
import com.example.resonance.model.schema.dto.CompanyDto

fun Company.toDto() = CompanyDto(
    id = id,
    companyName = companyName,
    companyDescription = companyDescription,
    industry = industry,
    websiteLink = websiteLink,
    careerPageLink = careerPageLink,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun UpsertCompanyRq.toEntity() = Company(
    companyName = companyName,
    companyDescription = companyDescription,
    industry = industry,
    websiteLink = websiteLink,
    careerPageLink = careerPageLink,
)