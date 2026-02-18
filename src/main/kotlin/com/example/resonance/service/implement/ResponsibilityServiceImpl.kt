package com.example.resonance.service.implement

import com.example.resonance.database.dao.ResponsibilityDao
import com.example.resonance.database.entity.Responsibility
import com.example.resonance.errors.DoesntHaveException
import com.example.resonance.errors.NotFountException
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.ResponsibilityDto
import com.example.resonance.model.schema.request.UpsertResponsibilityRq
import com.example.resonance.service.ResponsibilityService
import com.example.resonance.service.VacancyService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class ResponsibilityServiceImpl(
    private val responsibilityDao: ResponsibilityDao,
    private val vacancyService: VacancyService,
): ResponsibilityService {
    override fun getResponsibilityByVacancyId(vacancyId: UUID): List<ResponsibilityDto> =
        responsibilityDao.findResponsibilitiesByVacancies(vacancyService.getVacancy(vacancyId)).map { it.toDto() }

    override fun getResponsibility(id: UUID): Responsibility =
        responsibilityDao.findById(id).getOrElse { throw NotFountException("Обязанность", id) }

    override fun getResponsibilities(): List<ResponsibilityDto> = responsibilityDao.findAll().map { it.toDto() }

    override fun addResponsibility(
        vacancyId: UUID,
        rq: UpsertResponsibilityRq
    ): ResponsibilityDto {
        val responsibility = rq.toEntity()
        for (resp in responsibilityDao.findAll()) {
            if (responsibility == resp) {
                return updateResponsibility(resp.id!!, rq, vacancyId)
            }
        }
        return createResponsibility(rq, vacancyId)
    }

    override fun changeResponsibility(
        id: UUID,
        rq: UpsertResponsibilityRq,
        vacancyId: UUID
    ): ResponsibilityDto {
        val responsibility = rq.toEntity()
        if (responsibility == getResponsibility(id)) return getResponsibility(id).toDto()
        deleteResponsibility(id, vacancyId)
        for (resp in responsibilityDao.findAll()) {
            if (responsibility == resp) {
                return updateResponsibility(resp.id!!, rq, vacancyId)
            }
        }
        return createResponsibility(rq, vacancyId)
    }

    override fun deleteResponsibility(id: UUID, vacancyId: UUID) {
        if (!getResponsibility(id).vacancies.map { it.id }.contains(vacancyId)) {
            throw DoesntHaveException("Вакансия", "обязанность", vacancyId, id)
        }
        val responsibility = getResponsibility(id)
        val vacancy = vacancyService.getVacancy(vacancyId)
        vacancy.responsibilities.remove(responsibility)
        responsibility.vacancies.remove(vacancy)
        if (responsibility.vacancies.isEmpty()) responsibilityDao.delete(responsibility)
    }

    private fun createResponsibility(rq: UpsertResponsibilityRq, vacancyId: UUID): ResponsibilityDto {
        val responsibility = rq.toEntity()
        return responsibilityDao.save(modifyResponsibility(responsibility, vacancyId)).toDto()
    }

    private fun updateResponsibility(
        id: UUID,
        rq: UpsertResponsibilityRq,
        vacancyId: UUID,
    ): ResponsibilityDto {
        val responsibility = getResponsibility(id).update(rq)
        return responsibilityDao.save(modifyResponsibility(responsibility, vacancyId)).toDto()
    }

    private fun modifyResponsibility(responsibility: Responsibility, vacancyId: UUID): Responsibility {
        if (vacancyId !in responsibility.vacancies.map { it.id }) {
            val vacancy = vacancyService.getVacancy(vacancyId)
            responsibility.vacancies.add(vacancy)
            vacancy.responsibilities.add(responsibility)
        }
        return responsibility
    }
}