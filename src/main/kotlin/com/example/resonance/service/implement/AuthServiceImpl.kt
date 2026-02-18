package com.example.resonance.service.implement

import com.example.resonance.database.dao.CompanyDao
import com.example.resonance.database.dao.StudentDao
import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.UserEntity
import com.example.resonance.database.entity.UserType
import com.example.resonance.errors.AlreadyExistsException
import com.example.resonance.errors.ApiError
import com.example.resonance.errors.DataRequiredException
import com.example.resonance.errors.PasswordsDontMatchesException
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.schema.dto.AuthDto
import com.example.resonance.model.schema.dto.RegisterDto
import com.example.resonance.model.schema.request.RegisterRq
import com.example.resonance.model.schema.request.AuthRq
import com.example.resonance.security.JwtUtil
import com.example.resonance.service.AuthService
import com.example.resonance.service.CompanyService
import com.example.resonance.service.StudentService
import org.springframework.http.HttpStatus
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
    private val jwtUtil: JwtUtil,
    private val studentDao: StudentDao,
    private val companyDao: CompanyDao
): AuthService {

    override fun authenticate(rq: AuthRq): AuthDto {
        val user = userDao.findByEmail(rq.email)
            ?: throw ApiError(HttpStatus.UNAUTHORIZED, "Неверный email")

        if (!passwordEncoder.matches(rq.password, user.password)) {
            throw ApiError(HttpStatus.UNAUTHORIZED, "Неверный пароль")
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

    override fun register(rq: RegisterRq): RegisterDto {
        validateRegistrationRequest(rq)

        if (userDao.existsByEmail(rq.email)) throw AlreadyExistsException(rq.email)

        val (profileId, profileDto) = createProfileWithExistingDto(rq)

        val user = createUserEntity(rq, profileId)
        val savedUser = userDao.save(user)

        val token = jwtUtil.generateToken(savedUser)

        return RegisterDto(
            id = savedUser.id!!,
            email = savedUser.email,
            userType = savedUser.userType,
            token = token,
            message = "${rq.userType} registered successfully",
            profileId = profileId,
            profileData = profileDto
        )
    }

    private fun validateRegistrationRequest(rq: RegisterRq) {
        if (rq.password != rq.confirmPassword) throw PasswordsDontMatchesException()

        when (rq.userType) {
            UserType.STUDENT -> {
                if (rq.studentData == null) throw DataRequiredException(UserType.STUDENT)
            }
            UserType.COMPANY -> {
                if (rq.companyData == null) throw DataRequiredException(UserType.COMPANY)
            }
        }
    }

    private fun createProfileWithExistingDto(rq: RegisterRq): Pair<UUID, Any> {
        return when (rq.userType) {
            UserType.STUDENT -> {
                val studentRq = rq.studentData!!
                if (studentRq.toEntity() in studentDao.findAll()) throw AlreadyExistsException(studentRq.toEntity())
                val student = studentService.createStudent(studentRq)
                Pair(student.id!!, student)
            }
            UserType.COMPANY -> {
                val companyRq = rq.companyData!!
                if (companyRq.toEntity() in companyDao.findAll()) throw AlreadyExistsException(companyRq.toEntity())
                val company = companyService.createCompany(companyRq)
                Pair(company.id!!, company)
            }
        }
    }

    private fun createUserEntity(rq: RegisterRq, profileId: UUID): UserEntity {
        val encryptedPassword = passwordEncoder.encode(rq.password)!!
        return UserEntity(
            userId = profileId,
            userType = rq.userType,
            email = rq.email,
            password = encryptedPassword,
            isActive = true
        )
    }
}