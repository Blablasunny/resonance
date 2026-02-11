package com.example.resonance.service.implement

import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.UserEntity
import com.example.resonance.database.entity.UserType
import com.example.resonance.model.schema.request.UpsertUserRq
import com.example.resonance.model.schema.dto.UserDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEmailDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.updateEmail
import com.example.resonance.model.mapper.updatePassword
import com.example.resonance.model.schema.dto.EmailDto
import com.example.resonance.model.schema.dto.UpdateEmailDto
import com.example.resonance.model.schema.request.EmailRq
import com.example.resonance.model.schema.request.UpdateEmailRq
import com.example.resonance.model.schema.request.UpdatePasswordRq
import com.example.resonance.security.JwtUtil
import com.example.resonance.service.CompanyService
import com.example.resonance.service.StudentService
import com.example.resonance.service.UserService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class UserServiceImpl(
    private val userDao: UserDao,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
): UserService {
    override fun getUsers(): List<EmailDto> =
        userDao.findAll().map { it.toEmailDto() }

    override fun getUser(id: UUID): UserEntity =
        userDao.findById(id).getOrElse { throw RuntimeException("User with id $id not found") }

    override fun getId(rq: EmailRq): UUID {
        return userDao.findByEmail(rq.email)?.id ?: throw RuntimeException("User with email ${rq.email} doesn't exist")
    }

    override fun getEmail(id: UUID): EmailDto {
        if (userDao.existsById(id)) return getUser(id).toEmailDto()
        else throw RuntimeException("User with id $id not found")
    }

    override fun updateEmail(id: UUID, rq: UpdateEmailRq): UpdateEmailDto {
        if (userDao.findByEmail(rq.email) != null) {
            throw RuntimeException("User with email ${rq.email} already exists")
        }
        var user = getUser(id)
        user = userDao.save(user.updateEmail(rq.email))
        val token = jwtUtil.generateToken(user)
        return UpdateEmailDto(
            message = "Email updated successfully",
            newToken = token,
            newEmail = rq.email
        )
    }

    override fun updatePassword(
        id: UUID,
        rq: UpdatePasswordRq
    ): EmailDto {
        val user = getUser(id)
        if (!passwordEncoder.matches(rq.oldPassword, user.password)) {
            throw BadCredentialsException("Invalid credentials")
        }
        if (rq.password != rq.confirmPassword) throw IllegalArgumentException("Passwords do not match")
        if (rq.password.length < 8) throw IllegalArgumentException("Password must be at least 8 characters")
        val encryptedPassword = passwordEncoder.encode(rq.password)!!
        userDao.save(user.updatePassword(encryptedPassword))
        return getUser(id).toEmailDto()
    }
}