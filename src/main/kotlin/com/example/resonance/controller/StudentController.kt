package com.example.resonance.controller

import com.example.resonance.database.entity.UserType
import com.example.resonance.model.schema.dto.OccupationOfInterestDto
import com.example.resonance.model.schema.dto.SkillDto
import com.example.resonance.model.schema.dto.SphereOfInterestDto
import com.example.resonance.model.schema.dto.StudentDto
import com.example.resonance.model.schema.dto.SubjectDto
import com.example.resonance.model.schema.request.GradesRq
import com.example.resonance.model.schema.request.IdsRq
import com.example.resonance.model.schema.request.UpsertAchievementRq
import com.example.resonance.model.schema.request.UpsertEducationRq
import com.example.resonance.model.schema.request.UpsertExperienceRq
import com.example.resonance.model.schema.request.UpsertOccupationOfInterestRq
import com.example.resonance.model.schema.request.UpsertSkillRq
import com.example.resonance.model.schema.request.UpsertSocialProfileRq
import com.example.resonance.model.schema.request.UpsertSphereOfInterestRq
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.request.UpsertSubjectRq
import com.example.resonance.service.AchievementService
import com.example.resonance.service.EducationService
import com.example.resonance.service.ExperienceService
import com.example.resonance.service.OccupationOfInterestService
import com.example.resonance.service.SkillOwner
import com.example.resonance.service.SkillService
import com.example.resonance.service.SocialProfileService
import com.example.resonance.service.SphereOfInterestService
import com.example.resonance.service.StudentService
import com.example.resonance.service.SubjectService
import jakarta.validation.Valid
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
    private val subjectService: SubjectService,
    private val sphereOfInterestService: SphereOfInterestService,
    private val occupationOfInterestService: OccupationOfInterestService,
    private val socialProfileService: SocialProfileService,
    private val skillService: SkillService,
) {
    @GetMapping
    fun getStudents(): List<StudentDto> = studentService.getStudents()

    @GetMapping("/{id}")
    fun getStudent(@PathVariable("id") id: UUID) = studentService.getStudentById(id)

    @PreAuthorize("@securityService.isStudentOwner(#id)")
    @PostMapping("/{id}")
    fun updateStudent(@PathVariable("id") id: UUID, @RequestBody rq: UpsertStudentRq) =
        studentService.updateStudent(id, rq)

    @PreAuthorize("@securityService.isStudentOwner(#id)")
    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable("id") id: UUID) = studentService.deleteStudent(id)


    @GetMapping("/grades")
    fun getGrades() = studentService.getGrades()

    @GetMapping("/grades/ids")
    fun getGrades(@RequestBody rq: GradesRq) = studentService.getStudentsByGrades(rq)


    @GetMapping("/educations/{studentId}")
    fun getEducationByStudentId(@PathVariable("studentId") studentId: UUID) =
        educationService.getEducationsByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/educations/{studentId}")
    fun addEducation(@PathVariable("studentId") studentId: UUID, @Valid @RequestBody rq: UpsertEducationRq) =
        educationService.addEducation(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/educations/{studentId}/{id}")
    fun changeEducation(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @Valid @RequestBody rq: UpsertEducationRq) =
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
    fun addAchievement(@PathVariable("studentId") studentId: UUID, @Valid @RequestBody rq: UpsertAchievementRq) =
        achievementService.addAchievement(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/achievements/{studentId}/{id}")
    fun changeAchievement(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @Valid @RequestBody rq: UpsertAchievementRq) =
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
    fun addExperience(@PathVariable("studentId") studentId: UUID, @Valid @RequestBody rq: UpsertExperienceRq) =
        experienceService.addExperience(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/experiences/{studentId}/{id}")
    fun changeExperience(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @Valid @RequestBody rq: UpsertExperienceRq) =
        experienceService.changeExperience(id, rq, studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/experiences/{studentId}/{id}")
    fun deleteExperience(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        experienceService.deleteExperience(id, studentId)


    @GetMapping("/subjects")
    fun getSubjects(): List<SubjectDto> = subjectService.getSubjects()

    @GetMapping("/subjects/ids")
    fun getSubjectsByIds(@RequestBody rq: IdsRq): List<SubjectDto> =
        subjectService.getSubjectsByIds(rq)

    @GetMapping("/subjects/{studentId}")
    fun getSubjectByStudentId(@PathVariable("studentId") studentId: UUID) =
        subjectService.getSubjectByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/subjects/{studentId}")
    fun addSubject(@PathVariable("studentId") studentId: UUID, @Valid @RequestBody rq: UpsertSubjectRq) =
        subjectService.addSubject(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/subjects/{studentId}/{id}")
    fun changeSubject(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @Valid @RequestBody rq: UpsertSubjectRq) =
        subjectService.changeSubject(id, rq, studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/subjects/{studentId}/{id}")
    fun deleteSubject(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        subjectService.deleteSubject(id, studentId)


    @GetMapping("/sphere-of-interests")
    fun getSpereOfInterests(): List<SphereOfInterestDto> = sphereOfInterestService.getSphereOfInterests()

    @GetMapping("/sphere-of-interests/ids")
    fun getSpereOfInterestsByIds(@RequestBody rq: IdsRq): List<SphereOfInterestDto> =
        sphereOfInterestService.getSphereOfInterestsByIds(rq)

    @GetMapping("/sphere-of-interests/{studentId}")
    fun getSphereOfInterestByStudentId(@PathVariable("studentId") studentId: UUID) =
        sphereOfInterestService.getSphereOfInterestByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/sphere-of-interests/{studentId}")
    fun addSphereOfInterest(@PathVariable("studentId") studentId: UUID, @Valid @RequestBody rq: UpsertSphereOfInterestRq) =
        sphereOfInterestService.addSphereOfInterest(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/sphere-of-interests/{studentId}/{id}")
    fun changeSphereOfInterest(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @Valid @RequestBody rq: UpsertSphereOfInterestRq) =
        sphereOfInterestService.changeSphereOfInterest(id, rq, studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/sphere-of-interests/{studentId}/{id}")
    fun deleteSphereOfInterest(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        sphereOfInterestService.deleteSphereOfInterest(id, studentId)


    @GetMapping("/occupation-of-interests")
    fun getOccupationOfInterests(): List<OccupationOfInterestDto> = occupationOfInterestService.getOccupationOfInterests()

    @GetMapping("/occupation-of-interests/ids")
    fun getOccupationOfInterestsByIds(@RequestBody rq: IdsRq): List<OccupationOfInterestDto> =
        occupationOfInterestService.getOccupationOfInterestsByIds(rq)

    @GetMapping("/occupation-of-interests/{studentId}")
    fun getOccupationOfInterestByStudentId(@PathVariable("studentId") studentId: UUID) =
        occupationOfInterestService.getOccupationOfInterestByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/occupation-of-interests/{studentId}")
    fun addOccupationOfInterest(@PathVariable("studentId") studentId: UUID, @Valid @RequestBody rq: UpsertOccupationOfInterestRq) =
        occupationOfInterestService.addOccupationOfInterest(studentId, rq)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/occupation-of-interests/{studentId}/{id}")
    fun changeOccupationOfInterest(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @Valid @RequestBody rq: UpsertOccupationOfInterestRq) =
        occupationOfInterestService.changeOccupationOfInterest(id, rq, studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/occupation-of-interests/{studentId}/{id}")
    fun deleteOccupationOfInterest(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        occupationOfInterestService.deleteOccupationOfInterest(id, studentId)


    @GetMapping("/social-profiles/{studentId}")
    fun getSocialProfileByStudentId(@PathVariable("studentId") studentId: UUID) =
        socialProfileService.getSocialProfileByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/social-profiles/{studentId}")
    fun addSocialProfile(@PathVariable("studentId") studentId: UUID, @Valid @RequestBody rq: UpsertSocialProfileRq) =
        socialProfileService.addSocialProfile(studentId, rq, UserType.STUDENT)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/social-profiles/{studentId}/{id}")
    fun changeSocialProfile(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @Valid @RequestBody rq: UpsertSocialProfileRq) =
        socialProfileService.changeSocialProfile(id, rq, studentId, UserType.STUDENT)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/social-profiles/{studentId}/{id}")
    fun deleteSocialProfile(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        socialProfileService.deleteSocialProfile(id, studentId, UserType.STUDENT)


    @GetMapping("/skills")
    fun getSkills(): List<SkillDto> = skillService.getSkills()

    @GetMapping("/skills/ids")
    fun getSkillsByIds(@RequestBody rq: IdsRq): List<SkillDto> =
        skillService.getSkillsByIds(rq)

    @GetMapping("/skills/{studentId}")
    fun getSkillByStudentId(@PathVariable("studentId") studentId: UUID) =
        skillService.getSkillByStudentId(studentId)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/skills/{studentId}")
    fun addSkill(@PathVariable("studentId") studentId: UUID, @Valid @RequestBody rq: UpsertSkillRq) =
        skillService.addSkill(studentId, rq, SkillOwner.STUDENT)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @PostMapping("/skills/{studentId}/{id}")
    fun changeSkill(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID, @Valid @RequestBody rq: UpsertSkillRq) =
        skillService.changeSkill(id, rq, studentId, SkillOwner.STUDENT)

    @PreAuthorize("@securityService.isStudentOwner(#studentId)")
    @DeleteMapping("/skills/{studentId}/{id}")
    fun deleteSkill(@PathVariable("studentId") studentId: UUID, @PathVariable("id") id: UUID) =
        skillService.deleteSkill(id, studentId, SkillOwner.STUDENT)
}