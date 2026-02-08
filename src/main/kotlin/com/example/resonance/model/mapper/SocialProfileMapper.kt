package com.example.resonance.model.mapper

import com.example.resonance.database.entity.SocialProfile
import com.example.resonance.model.schema.dto.SocialProfileDto
import com.example.resonance.model.schema.request.UpsertSocialProfileRq
import java.time.LocalDateTime

fun SocialProfile.toDto() = SocialProfileDto(
    id = id,
    platformLink = platformLink,
    platformName = platformName,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() },
    companies = companies.map { it.toDto() },
)

fun UpsertSocialProfileRq.toEntity() = SocialProfile(
    platformLink = platformLink,
    platformName = platformName,
    description = description,
)

fun SocialProfile.update(rq: UpsertSocialProfileRq): SocialProfile =
    this.apply {
        platformLink = rq.platformLink
        platformName = rq.platformName
        description = rq.description
        updatedAt = LocalDateTime.now()
    }