package com.example.resonance.service.implement

import com.example.resonance.database.dao.CompanyDao
import com.example.resonance.database.entity.Company
import com.example.resonance.model.schema.request.UpsertCompanyRq
import com.example.resonance.model.schema.dto.CompanyDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.service.CompanyService
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Service
class CompanyServiceImpl(
    private val companyDao: CompanyDao,
) : CompanyService {
    override fun getCompanies(): List<CompanyDto> =
        companyDao.findAll().map { it.toDto() }

    override fun createCompany(rq: UpsertCompanyRq): CompanyDto =
        companyDao.save(rq.toEntity()).toDto()

    override fun getCompany(id: UUID): Company =
        companyDao.findById(id).getOrElse { throw RuntimeException("company not found") }
}