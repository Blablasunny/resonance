package com.example.resonance.controller

import com.example.resonance.database.dao.StudentDao
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.schema.dto.StudentDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import com.example.resonance.model.schema.request.UpsertStudentRq
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
    private val studentService: StudentService,
    private val educationService: EducationService,
) {
    @GetMapping
    fun getStudents(): List<StudentDto> = studentService.getStudents()

    @GetMapping("/{id}")
    fun getStudent(@PathVariable("id") id: UUID) = studentService.getStudent(id).toDto()

    @PostMapping("/{id}")
    fun updateStudent(@PathVariable("id") id: UUID, @RequestBody rq: UpsertStudentRq) =
        studentService.updateStudent(id, rq)

    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable("id") id: UUID) = studentService.deleteStudent(id)


    @GetMapping("/educations/{studentId}")
    fun getEducationByStudentId(@PathVariable("studentId") studentId: UUID) =
        educationService.getEducationsByStudentId(studentId)

    @PostMapping("/educations/{studentId}")
    fun addEducation(@PathVariable("studentId") studentId: UUID, @RequestBody rq: UpsertEducationRq) =
        educationService.addEducation(studentId, rq)

    @PostMapping("/educations/{studentId}/{id}")
    fun updateEducation(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @RequestBody rq: UpsertEducationRq) =
        educationService.changeEducation(id, rq, studentId)

    @DeleteMapping("/educations/{studentId}/{id}")
    fun deleteEducation(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        educationService.delEducation(id, studentId)
}