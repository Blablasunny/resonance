package com.example.resonance.controller

import com.example.resonance.model.schema.dto.EmailDto
import com.example.resonance.model.schema.dto.UpdateEmailDto
import com.example.resonance.model.schema.request.EmailRq
import com.example.resonance.model.schema.request.UpdateEmailRq
import com.example.resonance.model.schema.request.UpdatePasswordRq
import com.example.resonance.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
) {
    @GetMapping
    fun getUsers(): List<EmailDto> = userService.getUsers()

    @GetMapping("/id")
    fun getId(@RequestBody rq: EmailRq) : UUID = userService.getId(rq)

    @GetMapping("/{id}")
    fun getEmail(@PathVariable id: UUID): EmailDto = userService.getEmail(id)

    @PreAuthorize("@securityService.isOwner(#id)")
    @PostMapping("/{id}/email")
    fun updateEmail(@PathVariable id: UUID, @RequestBody rq: UpdateEmailRq): UpdateEmailDto =
        userService.updateEmail(id, rq)

    @PreAuthorize("@securityService.isOwner(#id)")
    @PostMapping("/{id}/password")
    fun updatePassword(@PathVariable id: UUID, @RequestBody rq: UpdatePasswordRq): EmailDto =
        userService.updatePassword(id, rq)
}