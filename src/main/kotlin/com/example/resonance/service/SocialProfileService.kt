package com.example.resonance.service

import com.example.resonance.database.entity.SocialProfile
import com.example.resonance.database.entity.UserType
import com.example.resonance.model.schema.dto.SocialProfileDto
import com.example.resonance.model.schema.request.UpsertSocialProfileRq
import java.util.UUID

interface SocialProfileService {
    fun getSocialProfileByStudentId(studentId: UUID): List<SocialProfileDto>
    fun getSocialProfileByCompanyId(companyId: UUID): List<SocialProfileDto>
    fun getSocialProfile(id: UUID): SocialProfile
    fun getSocialProfiles(): List<SocialProfileDto>
    fun addSocialProfile(userId: UUID, rq: UpsertSocialProfileRq, userType: UserType): SocialProfileDto
    fun changeSocialProfile(id: UUID, rq: UpsertSocialProfileRq, userId: UUID, userType: UserType): SocialProfileDto
    fun deleteSocialProfile(id: UUID, userId: UUID, userType: UserType)
}