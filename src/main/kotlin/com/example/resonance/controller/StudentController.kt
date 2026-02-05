package com.example.resonance.controller

import com.example.resonance.database.dao.StudentDao
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.schema.dto.StudentDto
import com.example.resonance.model.schema.request.UpsertAchievementRq
import com.example.resonance.model.schema.request.UpsertEducationRq
import com.example.resonance.model.schema.request.UpsertExperienceRq
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.service.AchievementService
import com.example.resonance.service.EducationService
import com.example.resonance.service.ExperienceService
import com.example.resonance.service.StudentService
import org.springframework.security.access.prepost.PreAuthorize
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
    private val achievementService: AchievementService,
    private val experienceService: ExperienceService,
) {
    @GetMapping
    fun getStudents(): List<StudentDto> = studentService.getStudents()

    @GetMapping("/{id}")
    fun getStudent(@PathVariable("id") id: UUID) = studentService.getStudent(id).toDto()

    @PreAuthorize("@securityService.isStudentOwner(#id)")
    @PostMapping("/{id}")
    fun updateStudent(@PathVariable("id") id: UUID, @RequestBody rq: UpsertStudentRq) =
        studentService.updateStudent(id, rq)

    @PreAuthorize("@securityService.isStudentOwner(#id)")
    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable("id") id: UUID) = studentService.deleteStudent(id)


    @GetMapping("/educations/{studentId}")
    fun getEducationByStudentId(@PathVariable("studentId") studentId: UUID) =
        educationService.getEducationsByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/educations/{studentId}")
    fun addEducation(@PathVariable("studentId") studentId: UUID, @RequestBody rq: UpsertEducationRq) =
        educationService.addEducation(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/educations/{studentId}/{id}")
    fun changeEducation(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @RequestBody rq: UpsertEducationRq) =
        educationService.changeEducation(id, rq, studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/educations/{studentId}/{id}")
    fun deleteEducation(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        educationService.deleteEducation(id, studentId)


    @GetMapping("/achievements/{studentId}")
    fun getAchievementByStudentId(@PathVariable("studentId") studentId: UUID) =
        achievementService.getAchievementByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/achievements/{studentId}")
    fun addAchievement(@PathVariable("studentId") studentId: UUID, @RequestBody rq: UpsertAchievementRq) =
        achievementService.addAchievement(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/achievements/{studentId}/{id}")
    fun changeAchievement(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @RequestBody rq: UpsertAchievementRq) =
        achievementService.changeAchievement(id, rq, studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/achievements/{studentId}/{id}")
    fun deleteAchievement(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        achievementService.deleteAchievement(id, studentId)


    @GetMapping("/experiences/{studentId}")
    fun getExperienceByStudentId(@PathVariable("studentId") studentId: UUID) =
        experienceService.getExperienceByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/experiences/{studentId}")
    fun addExperience(@PathVariable("studentId") studentId: UUID, @RequestBody rq: UpsertExperienceRq) =
        experienceService.addExperience(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/experiences/{studentId}/{id}")
    fun changeExperience(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @RequestBody rq: UpsertExperienceRq) =
        experienceService.changeExperience(id, rq, studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/experiences/{studentId}/{id}")
    fun deleteExperience(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        experienceService.deleteExperience(id, studentId)
}