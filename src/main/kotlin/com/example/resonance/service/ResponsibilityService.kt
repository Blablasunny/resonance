package com.example.resonance.service

import com.example.resonance.database.entity.Responsibility
import com.example.resonance.model.schema.dto.ResponsibilityDto
import com.example.resonance.model.schema.request.UpsertResponsibilityRq
import java.util.UUID

interface ResponsibilityService {
    fun getResponsibilityByVacancyId(vacancyId: UUID): List<ResponsibilityDto>
    fun getResponsibility(id: UUID): Responsibility
    fun getResponsibilities(): List<ResponsibilityDto>
    fun addResponsibility(vacancyId: UUID, rq: UpsertResponsibilityRq): ResponsibilityDto
    fun changeResponsibility(id: UUID, rq: UpsertResponsibilityRq, vacancyId: UUID): ResponsibilityDto
    fun deleteResponsibility(id: UUID, vacancyId: UUID)
}