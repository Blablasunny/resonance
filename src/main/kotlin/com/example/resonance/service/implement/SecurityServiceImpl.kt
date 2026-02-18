package com.example.resonance.service.implement

import com.example.resonance.database.entity.UserEntity
import com.example.resonance.database.entity.UserType
import com.example.resonance.errors.NotFountException
import com.example.resonance.service.CompanyService
import com.example.resonance.service.SecurityService
import com.example.resonance.service.StudentService
import com.example.resonance.service.VacancyService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service("securityService")
class SecurityServiceImpl(
    private val studentService: StudentService,
    private val companyService: CompanyService,
    private val vacancyService: VacancyService,
): SecurityService {
    override fun isOwner(
        id: UUID,
        authentication: Authentication
    ): Boolean {
        val currentUser = authentication.principal as? UserEntity ?: return false
        return currentUser.id == id
    }

    override fun isOwner(id: UUID): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication ?: return false
        return isOwner(id, authentication)
    }

    override fun isStudentOwner(studentId: UUID, authentication: Authentication): Boolean {
        val currentUser = authentication.principal as? UserEntity ?: return false

        return when (currentUser.userType) {
            UserType.STUDENT -> {
                val studentProfileId = getStudentProfileId(currentUser.userId)
                studentProfileId == studentId
            }
            UserType.COMPANY -> {
                false
            }
        }
    }

    override fun isStudentOwner(studentId: UUID): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication ?: return false
        return isStudentOwner(studentId, authentication)
    }

    override fun isCompanyOwner(companyId: UUID, authentication: Authentication): Boolean {
        val currentUser = authentication.principal as? UserEntity ?: return false

        return when (currentUser.userType) {
            UserType.COMPANY -> {
                val companyProfileId = getCompanyProfileId(currentUser.userId)
                companyProfileId == companyId
            }
            UserType.STUDENT -> {
                false
            }
        }
    }

    override fun isCompanyOwner(companyId: UUID): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication ?: return false
        return isCompanyOwner(companyId, authentication)
    }

    override fun isVacancyOwner(
        vacancyId: UUID,
        authentication: Authentication
    ): Boolean {
        val currentUser = authentication.principal as? UserEntity ?: return false

        return when (currentUser.userType) {
            UserType.COMPANY -> {
                val companyProfileId =
                    getCompanyProfileId(currentUser.userId) ?: throw NotFountException("Компания", currentUser.userId)
                vacancyId in vacancyService.getVacancyByCompanyId(companyProfileId).map { it.id }
            }
            UserType.STUDENT -> {
                false
            }
        }
    }

    override fun isVacancyOwner(vacancyId: UUID): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication ?: return false
        return isVacancyOwner(vacancyId, authentication)
    }

    private fun getStudentProfileId(userId: UUID): UUID? {
        return studentService.getStudent(userId).id
    }

    private fun getCompanyProfileId(userId: UUID): UUID? {
        return companyService.getCompany(userId).id
    }
}