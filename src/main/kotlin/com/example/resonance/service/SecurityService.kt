package com.example.resonance.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.UUID

interface SecurityService {
    fun isOwner(id: UUID, authentication: Authentication): Boolean
    fun isOwner(id: UUID): Boolean
    fun isStudentOwner(studentId: UUID, authentication: Authentication): Boolean
    fun isCompanyOwner(companyId: UUID, authentication: Authentication): Boolean
    fun isStudentOwner(studentId: UUID): Boolean
    fun isCompanyOwner(companyId: UUID): Boolean
}