package com.example.resonance.service.implement

import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.UserEntity
import com.example.resonance.database.entity.UserType
import com.example.resonance.model.schema.dto.AuthDto
import com.example.resonance.model.schema.dto.RegisterDto
import com.example.resonance.model.schema.request.RegisterRq
import com.example.resonance.model.schema.request.UpsertAuthRq
import com.example.resonance.security.JwtUtil
import com.example.resonance.service.AuthService
import com.example.resonance.service.CompanyService
import com.example.resonance.service.StudentService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthServiceImpl(
    private val userDao: UserDao,
    private val studentService: StudentService,
    private val companyService: CompanyService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
): AuthService {

    override fun authenticate(request: UpsertAuthRq): AuthDto {
        val user = userDao.findByEmail(request.email)
            ?: throw BadCredentialsException("Invalid credentials")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("Invalid credentials")
        }

        val token = jwtUtil.generateToken(user)

        return AuthDto(
            token = token,
            userId = user.userId,
            userType = user.userType,
            email = user.email
        )
    }

    override fun validateToken(token: String): Boolean {
        return jwtUtil.validateToken(token)
    }

    override fun register(request: RegisterRq): RegisterDto {
        validateRegistrationRequest(request)

        if (userDao.existsByEmail(request.email)) {
            throw IllegalArgumentException("User with email ${request.email} already exists")
        }

        val (profileId, profileDto) = createProfileWithExistingDto(request)

        val user = createUserEntity(request, profileId)
        val savedUser = userDao.save(user)

        val token = jwtUtil.generateToken(savedUser)

        return RegisterDto(
            id = savedUser.id!!,
            email = savedUser.email,
            userType = savedUser.userType,
            token = token,
            message = "${request.userType} registered successfully",
            profileId = profileId,
            profileData = profileDto
        )
    }

    private fun validateRegistrationRequest(request: RegisterRq) {
        if (request.password != request.confirmPassword) {
            throw IllegalArgumentException("Passwords do not match")
        }

        if (request.password.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters")
        }

        when (request.userType) {
            UserType.STUDENT -> {
                if (request.studentData == null) {
                    throw IllegalArgumentException("Student data is required for STUDENT type")
                }
            }
            UserType.COMPANY -> {
                if (request.companyData == null) {
                    throw IllegalArgumentException("Company data is required for COMPANY type")
                }
            }
        }
    }

    private fun createProfileWithExistingDto(request: RegisterRq): Pair<UUID, Any> {
        return when (request.userType) {
            UserType.STUDENT -> {
                val studentRq = request.studentData!!
                val student = studentService.createStudent(studentRq)
                Pair(student.id!!, student)
            }
            UserType.COMPANY -> {
                val companyRq = request.companyData!!
                val company = companyService.createCompany(companyRq)
                Pair(company.id!!, company)
            }
        }
    }

    private fun createUserEntity(request: RegisterRq, profileId: UUID): UserEntity {
        val encryptedPassword = passwordEncoder.encode(request.password)!!
        return UserEntity(
            userId = profileId,
            userType = request.userType,
            email = request.email,
            password = encryptedPassword,
            isActive = true
        )
    }
}