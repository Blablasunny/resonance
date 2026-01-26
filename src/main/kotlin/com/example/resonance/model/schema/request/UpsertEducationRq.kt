package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.EducationLevel
import java.util.UUID

data class UpsertEducationRq(
    val institutionName : String,
    val educationLevel: EducationLevel,
    val speciality: String,
    val status: Boolean = true,
    val studentIds: List<UUID>
)
