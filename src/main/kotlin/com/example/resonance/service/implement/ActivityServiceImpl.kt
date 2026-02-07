package com.example.resonance.service.implement

import com.example.resonance.database.dao.ActivityDao
import com.example.resonance.database.entity.Activity
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.ActivityDto
import com.example.resonance.model.schema.request.UpsertActivityRq
import com.example.resonance.service.ActivityService
import com.example.resonance.service.CompanyService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class ActivityServiceImpl(
    private val activityDao: ActivityDao,
    private val companyService: CompanyService
): ActivityService {
    override fun getActivityByCompanyId(companyId: UUID): List<ActivityDto> =
        activityDao.findActivitiesByCompanies(companyService.getCompany(companyId)).map { it.toDto() }

    override fun getActivity(id: UUID): Activity =
        activityDao.findById(id).getOrElse { throw RuntimeException("Activity with id $id found!") }

    override fun getActivities(): List<ActivityDto> = activityDao.findAll().map { it.toDto() }

    override fun addActivity(
        companyId: UUID,
        rq: UpsertActivityRq
    ): ActivityDto {
        val activity = rq.toEntity()
        for (act in activityDao.findAll()) {
            if (activity == act) {
                return updateActivity(act.id!!, rq, companyId)
            }
        }
        return createActivity(rq, companyId)
    }

    override fun changeActivity(
        id: UUID,
        rq: UpsertActivityRq,
        companyId: UUID
    ): ActivityDto {
        val activity = rq.toEntity()
        deleteActivity(id, companyId)
        for (act in activityDao.findAll()) {
            if (activity == act) {
                return updateActivity(act.id!!, rq, companyId)
            }
        }
        return createActivity(rq, companyId)
    }

    override fun deleteActivity(id: UUID, companyId: UUID) {
        if (!getActivity(id).companies.map { it.id }.contains(companyId)) {
            throw RuntimeException("Company with id $companyId doesn't have activity with id $id")
        }
        val activity = getActivity(id)
        val company = companyService.getCompany(companyId)
        company.activities.remove(activity)
        activity.companies.remove(company)
    }

    private fun createActivity(rq: UpsertActivityRq, companyId: UUID): ActivityDto {
        val activity = rq.toEntity()
        return activityDao.save(modifyActivity(activity, companyId)).toDto()
    }

    private fun updateActivity(
        id: UUID,
        rq: UpsertActivityRq,
        companyId: UUID
    ): ActivityDto {
        val activity = getActivity(id).update(rq)
        return activityDao.save(modifyActivity(activity, companyId)).toDto()
    }

    private fun modifyActivity(activity: Activity, companyId: UUID): Activity {
        if (companyId !in activity.companies.map { it.id }) {
            val company = companyService.getCompany(companyId)
            activity.companies.add(company)
            company.activities.add(activity)
        }
        return activity
    }
}