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
import com.example.resonance.model.schema.dto.AuthResponseDto
import com.example.resonance.model.schema.dto.RefreshTokenResponseDto
import com.example.resonance.model.schema.dto.RegisterDto
import com.example.resonance.model.schema.request.RegisterRq
import com.example.resonance.model.schema.request.AuthRq
import com.example.resonance.model.schema.request.RefreshTokenRq
import com.example.resonance.security.JwtUtil
import com.example.resonance.service.AuthService
import com.example.resonance.service.CompanyService
import com.example.resonance.service.StudentService
import com.example.resonance.service.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class AuthServiceImpl(
    private val userDao: UserDao,
    private val studentService: StudentService,
    private val companyService: CompanyService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val tokenService: TokenService,
    private val studentDao: StudentDao,
    private val companyDao: CompanyDao
): AuthService {

    @Transactional
    override fun authenticate(rq: AuthRq, request: HttpServletRequest): AuthResponseDto {
        val user = userDao.findByEmail(rq.email)
            ?: throw ApiError(HttpStatus.UNAUTHORIZED, "Неверный email")

        if (!passwordEncoder.matches(rq.password, user.password)) {
            throw ApiError(HttpStatus.UNAUTHORIZED, "Неверный пароль")
        }

        if (!user.isActive) {
            throw ApiError(HttpStatus.FORBIDDEN, "Аккаунт деактивирован")
        }

        return generateTokenPair(user, request)
    }

    override fun validateToken(token: String): Boolean {
        if (!jwtUtil.validateAccessToken(token)) {
            return false
        }

        if (tokenService.isTokenBlacklisted(token)) {
            return false
        }

        return true
    }

    @Transactional
    override fun register(rq: RegisterRq, request: HttpServletRequest): RegisterDto {
        validateRegistrationRequest(rq)

        if (userDao.existsByEmail(rq.email)) throw AlreadyExistsException(rq.email)

        val (profileId, profileDto) = createProfileWithExistingDto(rq)

        val user = createUserEntity(rq, profileId)
        val savedUser = userDao.save(user)

        val tokens = generateTokenPair(savedUser, request)

        return RegisterDto(
            id = savedUser.id!!,
            email = savedUser.email,
            userType = savedUser.userType,
            token = tokens.accessToken,
            message = "${rq.userType} registered successfully",
            profileId = profileId,
            profileData = profileDto
        )
    }

    @Transactional
    override fun refreshToken(rq: RefreshTokenRq, request: HttpServletRequest): RefreshTokenResponseDto {

        val refreshToken = rq.refreshToken

        val validToken = tokenService.validateRefreshToken(refreshToken)
            ?: throw ApiError(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token")

        val userId = validToken.userId
        val user = userDao.findByUserId(userId)
            ?: throw ApiError(HttpStatus.NOT_FOUND, "User not found")

        if (!user.isActive) {
            throw ApiError(HttpStatus.FORBIDDEN, "Account is deactivated")
        }

        tokenService.revokeRefreshToken(refreshToken)

        val newTokens = generateTokenPair(user, request)

        return RefreshTokenResponseDto(
            accessToken = newTokens.accessToken,
            refreshToken = newTokens.refreshToken,
            tokenType = "Bearer",
            expiresIn = jwtUtil.getAccessTokenExpirationMillis()
        )
    }

    @Transactional
    override fun logout(token: String, refreshToken: String?) {
        val userId = jwtUtil.extractUserId(token)
            ?: throw ApiError(HttpStatus.BAD_REQUEST, "Invalid token")

        tokenService.blacklistToken(token, userId, "USER_LOGOUT")

        refreshToken?.let {
            tokenService.revokeRefreshToken(it)
        }
    }

    @Transactional
    override fun logoutAll(userId: UUID) {
        tokenService.revokeAllUserRefreshTokens(userId)
    }

    private fun generateTokenPair(user: UserEntity, request: HttpServletRequest): AuthResponseDto {
        val accessToken = jwtUtil.generateAccessToken(user)
        val refreshToken = jwtUtil.generateRefreshToken(user)

        val expiresAt = LocalDateTime.now()
            .plusSeconds(jwtUtil.getRefreshTokenExpirationMillis() / 1000)

        tokenService.saveRefreshToken(
            token = refreshToken,
            userId = user.userId,
            expiresAt = expiresAt,
            ipAddress = tokenService.extractIpAddress(request),
            userAgent = tokenService.extractUserAgent(request)
        )

        return AuthResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = "Bearer",
            expiresIn = jwtUtil.getAccessTokenExpirationMillis(),
            userId = user.userId,
            userType = user.userType,
            email = user.email
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