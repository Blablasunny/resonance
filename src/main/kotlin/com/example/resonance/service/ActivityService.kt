package com.example.resonance.service

import com.example.resonance.database.entity.Activity
import com.example.resonance.model.schema.dto.ActivityDto
import com.example.resonance.model.schema.request.UpsertActivityRq
import java.util.UUID

interface ActivityService {
    fun getActivityByCompanyId(companyId: UUID): List<ActivityDto>
    fun getActivity(id: UUID): Activity
    fun getActivities(): List<ActivityDto>
    fun addActivity(companyId: UUID, rq: UpsertActivityRq): ActivityDto
    fun changeActivity(id: UUID, rq: UpsertActivityRq, companyId: UUID): ActivityDto
    fun deleteActivity(id: UUID, companyId: UUID)
}