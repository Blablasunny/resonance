package com.example.resonance.controller

import com.example.resonance.model.dto.rq.UpsertStudentRq
import com.example.resonance.model.dto.rs.StudentDto
import com.example.resonance.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/students")
class StudentController(
    private val service: StudentService,
) {
    @GetMapping
    fun getStudents(): List<StudentDto> = service.getStudents()

    @PostMapping
    fun createStudent(@RequestBody rq: UpsertStudentRq) = service.createStudent(rq)
}