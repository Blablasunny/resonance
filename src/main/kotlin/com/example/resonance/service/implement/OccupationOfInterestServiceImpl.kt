package com.example.resonance.service.implement

import com.example.resonance.database.dao.OccupationOfInterestDao
import com.example.resonance.database.entity.OccupationOfInterest
import com.example.resonance.errors.DoesntHaveException
import com.example.resonance.errors.NotFountException
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.OccupationOfInterestDto
import com.example.resonance.model.schema.dto.SphereOfInterestDto
import com.example.resonance.model.schema.request.IdsRq
import com.example.resonance.model.schema.request.UpsertOccupationOfInterestRq
import com.example.resonance.service.OccupationOfInterestService
import com.example.resonance.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class OccupationOfInterestServiceImpl(
    private val studentService: StudentService,
    private val occupationOfInterestDao: OccupationOfInterestDao,
): OccupationOfInterestService {
    override fun getOccupationOfInterestByStudentId(studentId: UUID): List<OccupationOfInterestDto> =
        occupationOfInterestDao.findOccupationsOfInterestByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getOccupationOfInterest(id: UUID): OccupationOfInterest =
        occupationOfInterestDao.findById(id).getOrElse { throw NotFountException("Профессиональный интерес", id) }

    override fun getOccupationOfInterestById(id: UUID): OccupationOfInterestDto = getOccupationOfInterest(id).toDto()

    override fun getOccupationOfInterests(): List<OccupationOfInterestDto> = occupationOfInterestDao.findAll().map { it.toDto() }

    override fun getOccupationOfInterestsByIds(rq: IdsRq): List<OccupationOfInterestDto> {
        val occupationOfInterests: MutableList<OccupationOfInterestDto> = arrayListOf()
        for (id in rq.ids) {
            val occupationOfInterestDto = getOccupationOfInterestById(id)
            if (occupationOfInterestDto !in occupationOfInterests)
                occupationOfInterests.add(occupationOfInterestDto)
        }
        return occupationOfInterests
    }

    override fun addOccupationOfInterest(
        studentId: UUID,
        rq: UpsertOccupationOfInterestRq
    ): OccupationOfInterestDto {
        val occupationOfInterest = rq.toEntity()
        for (sphere in occupationOfInterestDao.findAll()) {
            if (occupationOfInterest == sphere) {
                return updateOccupationOfInterest(sphere.id!!, rq, studentId)
            }
        }
        return createOccupationOfInterest(rq, studentId)
    }

    override fun changeOccupationOfInterest(
        id: UUID,
        rq: UpsertOccupationOfInterestRq,
        studentId: UUID
    ): OccupationOfInterestDto {
        val occupationOfInterest = rq.toEntity()
        if (occupationOfInterest == getOccupationOfInterest(id)) return getOccupationOfInterestById(id)
        deleteOccupationOfInterest(id, studentId)
        for (sphere in occupationOfInterestDao.findAll()) {
            if (occupationOfInterest == sphere) {
                return updateOccupationOfInterest(sphere.id!!, rq, studentId)
            }
        }
        return createOccupationOfInterest(rq, studentId)
    }

    override fun deleteOccupationOfInterest(id: UUID, studentId: UUID) {
        if (!getOccupationOfInterest(id).students.map { it.id }.contains(studentId)) {
            throw DoesntHaveException("Студент", "профессиональный интерес", studentId, id)
        }
        val occupationOfInterest = getOccupationOfInterest(id)
        val student = studentService.getStudent(studentId)
        student.occupationOfInterests.remove(occupationOfInterest)
        occupationOfInterest.students.remove(student)
        if (occupationOfInterest.students.isEmpty()) occupationOfInterestDao.delete(occupationOfInterest)
    }

    private fun createOccupationOfInterest(rq: UpsertOccupationOfInterestRq, studentId: UUID): OccupationOfInterestDto {
        val occupationOfInterest = rq.toEntity()
        return occupationOfInterestDao.save(modifyOccupationOfInterest(occupationOfInterest, studentId)).toDto()
    }

    private fun updateOccupationOfInterest(
        id: UUID,
        rq: UpsertOccupationOfInterestRq,
        studentId: UUID
    ): OccupationOfInterestDto {
        val occupationOfInterest = getOccupationOfInterest(id).update(rq)
        return occupationOfInterestDao.save(modifyOccupationOfInterest(occupationOfInterest, studentId)).toDto()
    }

    private fun modifyOccupationOfInterest(occupationOfInterest: OccupationOfInterest, studentId: UUID): OccupationOfInterest {
        if (studentId !in occupationOfInterest.students.map { it.id }) {
            val student = studentService.getStudent(studentId)
            occupationOfInterest.students.add(student)
            student.occupationOfInterests.add(occupationOfInterest)
        }
        return occupationOfInterest
    }
}