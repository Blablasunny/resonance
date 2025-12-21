package com.example.resonance.service.impl

import com.example.resonance.database.dao.UserDao
import com.example.resonance.model.dto.rq.UpsertUserRq
import com.example.resonance.model.dto.rs.UserDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userDao: UserDao,
): UserService {
    override fun getUsers(): List<UserDto> =
        userDao.findAll().map { it.toDto() }

    override fun createUser(rq: UpsertUserRq): UserDto =
        userDao.save(rq.toEntity()).toDto()
}