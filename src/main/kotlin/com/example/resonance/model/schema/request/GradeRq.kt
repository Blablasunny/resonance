package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.ProfessionGrade

data class GradeRq(
    val grades: List<ProfessionGrade>
)
