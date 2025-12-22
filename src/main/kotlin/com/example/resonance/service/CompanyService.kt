package com.example.resonance.service

import com.example.resonance.database.entity.Company
import com.example.resonance.model.schema.request.UpsertCompanyRq
import com.example.resonance.model.schema.dto.CompanyDto
import java.util.UUID

interface CompanyService {
    fun getCompanies(): List<CompanyDto>
    fun createCompany(rq: UpsertCompanyRq): CompanyDto
    fun getCompany(id : UUID): Company
}