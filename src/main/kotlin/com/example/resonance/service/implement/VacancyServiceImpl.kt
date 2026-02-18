package com.example.resonance.service.implement

import com.example.resonance.database.dao.ResponsibilityDao
import com.example.resonance.database.dao.SkillDao
import com.example.resonance.database.dao.VacancyDao
import com.example.resonance.database.entity.Vacancy
import com.example.resonance.errors.DoesntHaveException
import com.example.resonance.errors.NotFountException
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.VacancyDto
import com.example.resonance.model.schema.request.UpsertVacancyRq
import com.example.resonance.service.CompanyService
import com.example.resonance.service.VacancyService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class VacancyServiceImpl(
    private val vacancyDao: VacancyDao,
    private val companyService: CompanyService,
    private val skillDao: SkillDao,
    private val responsibilityDao: ResponsibilityDao,
): VacancyService {
    override fun getVacancyByCompanyId(companyId: UUID): List<VacancyDto> =
        vacancyDao.findVacanciesByCompanies(companyService.getCompany(companyId)).map { it.toDto() }

    override fun getOpenVacancyByCompanyId(companyId: UUID): List<VacancyDto> =
        vacancyDao.findVacanciesByCompanies(companyService.getCompany(companyId)).filter { it.isOpen }.map { it.toDto() }

    override fun getVacancy(id: UUID): Vacancy =
        vacancyDao.findById(id).getOrElse { throw NotFountException("Вакансия", id) }

    override fun getVacancyById(id: UUID): VacancyDto = getVacancy(id).toDto()

    override fun getVacancies(): List<VacancyDto> = vacancyDao.findAll().map { it.toDto() }

    override fun getOpenVacancies(): List<VacancyDto> =
        vacancyDao.findAll().filter { it.isOpen }.map {it.toDto() }

    override fun addVacancy(
        companyId: UUID,
        rq: UpsertVacancyRq
    ): VacancyDto {
        val vacancy = rq.toEntity()
        for (vac in vacancyDao.findAll()) {
            if (vacancy == vac) {
                return updateVacancy(vac.id!!, rq, companyId)
            }
        }
        return createVacancy(rq, companyId)
    }

    override fun changeVacancy(
        id: UUID,
        rq: UpsertVacancyRq,
        companyId: UUID
    ): VacancyDto {
        val vacancy = rq.toEntity()
        if (vacancy == getVacancy(id)) getVacancyById(id)
        deleteVacancy(id, companyId)
        for (vac in vacancyDao.findAll()) {
            if (vacancy == vac) {
                return updateVacancy(vac.id!!, rq, companyId)
            }
        }
        return createVacancy(rq, companyId)
    }

    override fun deleteVacancy(id: UUID, companyId: UUID) {
        if (!getVacancy(id).companies.map { it.id }.contains(companyId)) {
            throw DoesntHaveException("Компания", "вакансия", companyId, id)
        }
        val vacancy = getVacancy(id)
        val company = companyService.getCompany(companyId)
        company.vacancies.remove(vacancy)
        vacancy.companies.remove(company)
        val skills = vacancy.skills
        val responsibilities = vacancy.responsibilities
        if (vacancy.companies.isEmpty()) vacancyDao.delete(vacancy)
        skills.forEach {if (it.vacancies.isEmpty() && it.students.isEmpty()) skillDao.delete(it) }
        responsibilities.forEach {if (it.vacancies.isEmpty()) responsibilityDao.delete(it) }
    }

    private fun createVacancy(rq: UpsertVacancyRq, companyId: UUID): VacancyDto {
        val vacancy = rq.toEntity()
        return vacancyDao.save(modifyVacancy(vacancy, companyId)).toDto()
    }

    private fun updateVacancy(
        id: UUID,
        rq: UpsertVacancyRq,
        companyId: UUID
    ): VacancyDto {
        val vacancy = getVacancy(id).update(rq)
        return vacancyDao.save(modifyVacancy(vacancy, companyId)).toDto()
    }

    private fun modifyVacancy(vacancy: Vacancy, companyId: UUID): Vacancy {
        if (companyId !in vacancy.companies.map { it.id }) {
            val company = companyService.getCompany(companyId)
            vacancy.companies.add(company)
            company.vacancies.add(vacancy)
        }
        return vacancy
    }
}