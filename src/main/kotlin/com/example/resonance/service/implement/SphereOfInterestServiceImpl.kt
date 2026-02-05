package com.example.resonance.service.implement

import com.example.resonance.database.dao.SphereOfInterestDao
import com.example.resonance.database.entity.SphereOfInterest
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.SphereOfInterestDto
import com.example.resonance.model.schema.request.UpsertSphereOfInterestRq
import com.example.resonance.service.SphereOfInterestService
import com.example.resonance.service.StudentService
import com.sun.security.jgss.GSSUtil.createSubject
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class SphereOfInterestServiceImpl(
    private val sphereOfInterestDao: SphereOfInterestDao,
    private val studentService: StudentService,
): SphereOfInterestService {
    override fun getSphereOfInterestByStudentId(studentId: UUID): List<SphereOfInterestDto> =
        sphereOfInterestDao.findSphereOfInterestByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getSphereOfInterest(id: UUID): SphereOfInterest =
        sphereOfInterestDao.findById(id).getOrElse { throw RuntimeException("Sphere of interest with id $id not found!") }

    override fun getSphereOfInterests(): List<SphereOfInterestDto> = sphereOfInterestDao.findAll().map { it.toDto() }

    override fun addSphereOfInterest(
        studentId: UUID,
        rq: UpsertSphereOfInterestRq
    ): SphereOfInterestDto {
        val sphereOfInterest = rq.toEntity()
        for (sphere in sphereOfInterestDao.findAll()) {
            if (sphereOfInterest == sphere) {
                return updateSphereOfInterest(sphere.id!!, rq, studentId)
            }
        }
        return createSphereOfInterest(rq, studentId)
    }

    override fun changeSphereOfInterest(
        id: UUID,
        rq: UpsertSphereOfInterestRq,
        studentId: UUID
    ): SphereOfInterestDto {
        val sphereOfInterest = rq.toEntity()
        deleteSphereOfInterest(id, studentId)
        for (sphere in sphereOfInterestDao.findAll()) {
            if (sphereOfInterest == sphere) {
                return updateSphereOfInterest(sphere.id!!, rq, studentId)
            }
        }
        return createSphereOfInterest(rq, studentId)
    }

    override fun deleteSphereOfInterest(id: UUID, studentId: UUID) {
        if (!getSphereOfInterest(id).students.map { it.id }.contains(studentId)) {
            throw RuntimeException("Student with id $studentId doesn't have sphere of interest with id $id")
        }
        val spereOfInterest = getSphereOfInterest(id)
        val student = studentService.getStudent(studentId)
        student.sphereOfInterests.remove(spereOfInterest)
        spereOfInterest.students.remove(student)
    }

    private fun createSphereOfInterest(rq: UpsertSphereOfInterestRq, studentId: UUID): SphereOfInterestDto {
        val sphereOfInterest = rq.toEntity()
        return sphereOfInterestDao.save(modifySphereOfInterest(sphereOfInterest, studentId)).toDto()
    }

    private fun updateSphereOfInterest(
        id: UUID,
        rq: UpsertSphereOfInterestRq,
        studentId: UUID
    ): SphereOfInterestDto {
        val sphereOfInterest = getSphereOfInterest(id).update(rq)
        return sphereOfInterestDao.save(modifySphereOfInterest(sphereOfInterest, studentId)).toDto()
    }

    private fun modifySphereOfInterest(sphereOfInterest: SphereOfInterest, studentId: UUID): SphereOfInterest {
        if (studentId !in sphereOfInterest.students.map { it.id }) {
            val student = studentService.getStudent(studentId)
            sphereOfInterest.students.add(student)
            student.sphereOfInterests.add(sphereOfInterest)
        }
        return sphereOfInterest
    }
}