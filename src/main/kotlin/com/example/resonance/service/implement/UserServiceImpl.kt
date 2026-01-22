package com.example.resonance.service.implement

import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.UserEntity
import com.example.resonance.database.entity.UserType
import com.example.resonance.model.schema.request.UpsertUserRq
import com.example.resonance.model.schema.dto.UserDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.service.CompanyService
import com.example.resonance.service.StudentService
import com.example.resonance.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userDao: UserDao,
    private val studentService: StudentService,
    private val companyService: CompanyService,
    private val passwordEncoder: PasswordEncoder
): UserService {
    override fun getUsers(): List<UserDto> =
        userDao.findAll().map { it.toDto() }

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