package com.example.resonance.controller

import com.example.resonance.database.dao.StudentDao
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.dto.StudentDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import com.example.resonance.service.EducationService
import com.example.resonance.service.StudentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/students")
class StudentController(
    private val service: StudentService,
    private val educationService: EducationService,
    private val studentDao: StudentDao
) {
    @GetMapping
    fun getStudents(): List<StudentDto> = service.getStudents()

//    @PostMapping
//    fun createStudent(@RequestBody rq: UpsertStudentRq) = service.createStudent(rq)

    @GetMapping("/{id}")
    fun getStudent(@PathVariable("id") id: UUID) = service.getStudent(id).toDto()

    @GetMapping("/educations/{studentId}")
    fun getEducationByStudentId(@PathVariable("studentId") studentId: UUID) =
        educationService.getEducationsByStudentId(studentId)

    @PostMapping("/educations")
    fun createEducation(@RequestBody rq: UpsertEducationRq) =
        educationService.createEducation(rq)

    @PostMapping("/educations/{id}")
    fun createEducation(@PathVariable("id") id: UUID, @RequestBody rq: UpsertEducationRq) =
        educationService.updateEducation(id, rq)

    @DeleteMapping("/educations/{id}")
    fun deleteEducation(@PathVariable("id") id: UUID) = educationService.deleteEducation(id)
}