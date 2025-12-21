package com.example.resonance.controller

import com.example.resonance.model.dto.rq.UpsertUserRq
import com.example.resonance.model.dto.rs.UserDto
import com.example.resonance.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val service: UserService
) {
    @GetMapping
    fun getUsers(): List<UserDto> = service.getUsers()

    @PostMapping
    fun createUser(@RequestBody rq: UpsertUserRq) = service.createUser(rq)
}