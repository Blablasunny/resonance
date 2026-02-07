package com.example.resonance.service.implement

import com.example.resonance.database.dao.CompanyDao
import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.Company
import com.example.resonance.model.schema.request.UpsertCompanyRq
import com.example.resonance.model.schema.dto.CompanyDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.service.CompanyService
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Service
class CompanyServiceImpl(
    private val companyDao: CompanyDao,
    private val userDao: UserDao,
) : CompanyService {
    override fun getCompanies(): List<CompanyDto> =
        companyDao.findAll().map { it.toDto() }

    override fun getCompany(id: UUID): Company =
        companyDao.findById(id).getOrElse { throw RuntimeException("company not found") }

    override fun createCompany(rq: UpsertCompanyRq): CompanyDto =
        companyDao.save(rq.toEntity()).toDto()

    override fun updateCompany(
        id: UUID,
        rq: UpsertCompanyRq
    ): CompanyDto {
        val company = rq.toEntity()
        val oldCompany = getCompany(id)
        if (oldCompany == company) {
            return oldCompany.toDto()
        }
        if (company in companyDao.findAll()) {
            throw RuntimeException("this company already exists")
        }
        return companyDao.save(getCompany(id).update(rq)).toDto()
    }

    override fun deleteCompany(id: UUID) {
        val userId = userDao.findByUserId(id)!!.id
        companyDao.deleteById(id)
        userDao.deleteById(userId!!)
    }
}