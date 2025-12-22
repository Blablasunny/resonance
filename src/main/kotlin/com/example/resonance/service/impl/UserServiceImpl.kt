package com.example.resonance.service.impl

import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.UserEntity
import com.example.resonance.model.dto.rq.UpsertUserRq
import com.example.resonance.model.dto.rs.UserDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.service.StudentService
import com.example.resonance.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userDao: UserDao,
    private val studentService: StudentService
): UserService {
    override fun getUsers(): List<UserDto> =
        userDao.findAll().map { it.toDto() }

    override fun createUser(rq: UpsertUserRq): UserDto {
        val user = rq.toEntity()
        return userDao.save(modifyUser(user, rq)).toDto()
    }

    private fun modifyUser(user: UserEntity, rq: UpsertUserRq): UserEntity {
        val student = studentService.getStudent(rq.userId)
        userDao.apply {
            user.student = student
        }
        return user
    }
}