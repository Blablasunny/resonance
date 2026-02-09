package com.example.resonance.service

import com.example.resonance.database.entity.Vacancy
import com.example.resonance.model.schema.dto.VacancyDto
import com.example.resonance.model.schema.request.UpsertVacancyRq
import java.util.UUID

interface VacancyService{
    fun getVacancyByCompanyId(companyId: UUID): List<VacancyDto>
    fun getOpenVacancyByCompanyId(companyId: UUID): List<VacancyDto>
    fun getVacancy(id: UUID): Vacancy
    fun getVacancies(): List<VacancyDto>
    fun getOpenVacancies(): List<VacancyDto>
    fun addVacancy(companyId: UUID, rq: UpsertVacancyRq): VacancyDto
    fun changeVacancy(id: UUID, rq: UpsertVacancyRq, companyId: UUID): VacancyDto
    fun deleteVacancy(id: UUID, companyId: UUID)
}