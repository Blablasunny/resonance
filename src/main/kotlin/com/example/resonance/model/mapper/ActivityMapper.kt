package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Activity
import com.example.resonance.model.schema.dto.ActivityDto
import com.example.resonance.model.schema.request.UpsertActivityRq
import java.time.LocalDateTime

fun Activity.toDto() = ActivityDto(
    id = id,
    title = title,
    activityType = activityType,
    format = format,
    description = description,
    spheres = spheres,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt,
    companies = companies.map { it.toDto() }
)

fun UpsertActivityRq.toEntity() = Activity(
    title = title,
    activityType = activityType,
    format = format,
    description = description,
    spheres = spheres,
    date = date,
)

fun Activity.update(rq: UpsertActivityRq): Activity =
    this.apply {
        title = rq.title
        activityType = rq.activityType
        format = rq.format
        description = rq.description
        spheres = rq.spheres
        date = rq.date
        updatedAt = LocalDateTime.now()
    }