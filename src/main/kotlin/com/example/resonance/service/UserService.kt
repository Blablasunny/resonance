package com.example.resonance.service

import com.example.resonance.model.dto.rq.UpsertUserRq
import com.example.resonance.model.dto.rs.UserDto

interface UserService {
    fun getUsers() : List<UserDto>
    fun createUser(rq: UpsertUserRq): UserDto
}