package com.example.resonance.service.implement

import com.example.resonance.database.dao.SocialProfileDao
import com.example.resonance.database.entity.SocialProfile
import com.example.resonance.database.entity.UserType
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.SocialProfileDto
import com.example.resonance.model.schema.request.UpsertSocialProfileRq
import com.example.resonance.service.CompanyService
import com.example.resonance.service.SocialProfileService
import com.example.resonance.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class SocialProfileServiceImpl(
    private val socialProfileDao: SocialProfileDao,
    private val studentService: StudentService,
    private val companyService: CompanyService,
): SocialProfileService {
    override fun getSocialProfileByStudentId(studentId: UUID): List<SocialProfileDto> =
        socialProfileDao.findSocialProfilesByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getSocialProfileByCompanyId(companyId: UUID): List<SocialProfileDto> =
        socialProfileDao.findSocialProfilesByCompanies(companyService.getCompany(companyId)).map { it.toDto() }

    override fun getSocialProfile(id: UUID): SocialProfile =
        socialProfileDao.findById(id).getOrElse { throw RuntimeException("Social profile with id $id not found!") }

    override fun getSocialProfiles(): List<SocialProfileDto> = socialProfileDao.findAll().map { it.toDto() }

    override fun addSocialProfile(
        userId: UUID,
        rq: UpsertSocialProfileRq,
        userType: UserType
    ): SocialProfileDto {
        val socialProfile = rq.toEntity()
        for (social in socialProfileDao.findAll()) {
            if (socialProfile == social) {
                return updateSocialProfile(social.id!!, rq, userId, userType)
            }
        }
        return createSocialProfile(rq, userId, userType)
    }

    override fun changeSocialProfile(
        id: UUID,
        rq: UpsertSocialProfileRq,
        userId: UUID,
        userType: UserType
    ): SocialProfileDto {
        val socialProfile = rq.toEntity()
        if (socialProfile == getSocialProfile(id)) return getSocialProfile(id).toDto()
        deleteSocialProfile(id, userId, userType)
        for (social in socialProfileDao.findAll()) {
            if (socialProfile == social) {
                return updateSocialProfile(social.id!!, rq, userId, userType)
            }
        }
        return createSocialProfile(rq, userId, userType)
    }

    override fun deleteSocialProfile(id: UUID, userId: UUID, userType: UserType) {
        val socialProfile = getSocialProfile(id)
        when(userType) {
            UserType.STUDENT -> {
                if (!getSocialProfile(id).students.map { it.id }.contains(userId)) {
                    throw RuntimeException("Student with id $userId doesn't have social profile with id $id")
                }
                val student = studentService.getStudent(userId)
                student.socialProfiles.remove(socialProfile)
                socialProfile.students.remove(student)
            }

            UserType.COMPANY -> {
                if (!getSocialProfile(id).companies.map { it.id }.contains(userId)) {
                    throw RuntimeException("Company with id $userId doesn't have social profile with id $id")
                }
                val company = companyService.getCompany(userId)
                company.socialProfiles.remove(socialProfile)
                socialProfile.companies.remove(company)
            }
        }
        if (socialProfile.students.isEmpty() && socialProfile.companies.isEmpty()) socialProfileDao.delete(socialProfile)
    }

    private fun createSocialProfile(rq: UpsertSocialProfileRq, userId: UUID, userType: UserType): SocialProfileDto {
        val socialProfile = rq.toEntity()
        return socialProfileDao.save(modifySocialProfile(socialProfile, userId, userType)).toDto()
    }

    private fun updateSocialProfile(
        id: UUID,
        rq: UpsertSocialProfileRq,
        userId: UUID,
        userType: UserType
    ): SocialProfileDto {
        val socialProfile = getSocialProfile(id).update(rq)
        return socialProfileDao.save(modifySocialProfile(socialProfile, userId, userType)).toDto()
    }

    private fun modifySocialProfile(socialProfile: SocialProfile, userId: UUID, userType: UserType): SocialProfile {
        when(userType) {
            UserType.STUDENT -> {
                if (userId !in socialProfile.students.map { it.id }) {
                    val student = studentService.getStudent(userId)
                    socialProfile.students.add(student)
                    student.socialProfiles.add(socialProfile)
                }
            }

            UserType.COMPANY -> {
                if (userId !in socialProfile.companies.map { it.id }) {
                    val company = companyService.getCompany(userId)
                    socialProfile.companies.add(company)
                    company.socialProfiles.add(socialProfile)
                }
            }
        }
        return socialProfile
    }
}