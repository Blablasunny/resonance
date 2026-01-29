package com.example.resonance.service.implement

import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.UserEntity
import com.example.resonance.database.entity.UserType
import com.example.resonance.model.schema.request.UpsertUserRq
import com.example.resonance.model.schema.dto.UserDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.updateEmail
import com.example.resonance.service.CompanyService
import com.example.resonance.service.StudentService
import com.example.resonance.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Service
class UserServiceImpl(
    private val userDao: UserDao,
    private val studentService: StudentService,
    private val companyService: CompanyService,
    private val passwordEncoder: PasswordEncoder
): UserService {
    override fun getUsers(): List<String> =
        userDao.findAll().map { it.email }

    override fun getUser(id: UUID): UserEntity =
        userDao.findById(id).getOrElse { throw RuntimeException("User with id $id not found") }

    override fun getEmail(id: UUID): String {
        if (userDao.existsById(id)) return userDao.findProtectionById(id)!!.getEmail()
        else throw RuntimeException("User with id $id not found")
    }

    override fun updateEmail(id: UUID, email: String): String {
        if (userDao.findByEmail(email) != null) {
            throw RuntimeException("User with email $email already exists")
        }
        val user = getUser(id)
        userDao.save(user.updateEmail(email))
        return userDao.findProtectionById(id)!!.getEmail()
    }

    override fun createUser(rq: UpsertUserRq): UserDto {
        val encryptedPassword = passwordEncoder.encode(rq.getPasswordOrThrow())
        val user = rq.copy(password = encryptedPassword).toEntity()
        return userDao.save(modifyUser(user, rq)).toDto()
    }

    private fun modifyUser(user: UserEntity, rq: UpsertUserRq): UserEntity {
        when (rq.userType) {
            UserType.STUDENT -> {userDao.apply {
                user.userId = studentService.getStudent(rq.userId).id ?: throw RuntimeException("User not found")
            }}
            UserType.COMPANY -> {userDao.apply {
                user.userId = companyService.getCompany(rq.userId).id ?: throw RuntimeException("User not found")
            }}
        }
        return user
    }
}