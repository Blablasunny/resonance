package com.example.resonance.service.implement

import com.example.resonance.database.dao.SubjectDao
import com.example.resonance.database.entity.Subject
import com.example.resonance.errors.DoesntHaveException
import com.example.resonance.errors.NotFountException
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.SphereOfInterestDto
import com.example.resonance.model.schema.dto.SubjectDto
import com.example.resonance.model.schema.request.IdsRq
import com.example.resonance.model.schema.request.UpsertSubjectRq
import com.example.resonance.service.StudentService
import com.example.resonance.service.SubjectService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class SubjectServiceImpl(
    private val subjectDao: SubjectDao,
    private val studentService: StudentService,
): SubjectService {
    override fun getSubjectByStudentId(studentId: UUID): List<SubjectDto> =
        subjectDao.findSubjectsByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getSubject(id: UUID): Subject =
        subjectDao.findById(id).getOrElse { throw NotFountException("Учебный предмет", id) }

    override fun getSubjectById(id: UUID): SubjectDto = getSubject(id).toDto()

    override fun getSubjects(): List<SubjectDto> = subjectDao.findAll().map { it.toDto() }

    override fun getSubjectsByIds(rq: IdsRq): List<SubjectDto> {
        val subjects: MutableList<SubjectDto> = arrayListOf()
        for (id in rq.ids) {
            val subjectDto = getSubjectById(id)
            if (subjectDto !in subjects)
                subjects.add(subjectDto)
        }
        return subjects
    }

    override fun addSubject(
        studentId: UUID,
        rq: UpsertSubjectRq
    ): SubjectDto {
        val subject = rq.toEntity()
        for (sub in subjectDao.findAll()) {
            if (subject == sub) {
                return updateSubject(sub.id!!, rq, studentId)
            }
        }
        return createSubject(rq, studentId)
    }

    override fun changeSubject(
        id: UUID,
        rq: UpsertSubjectRq,
        studentId: UUID
    ): SubjectDto {
        val subject = rq.toEntity()
        if (subject == getSubject(id)) return getSubjectById(id)
        deleteSubject(id, studentId)
        for (sub in subjectDao.findAll()) {
            if (subject == sub) {
                return updateSubject(sub.id!!, rq, studentId)
            }
        }
        return createSubject(rq, studentId)
    }

    override fun deleteSubject(id: UUID, studentId: UUID) {
        if (!getSubject(id).students.map { it.id }.contains(studentId)) {
            throw DoesntHaveException("Студент", "учебный предмет", studentId, id)
        }
        val subject = getSubject(id)
        val student = studentService.getStudent(studentId)
        student.subjects.remove(subject)
        subject.students.remove(student)
        if (subject.students.isEmpty()) subjectDao.delete(subject)
    }

    private fun createSubject(rq: UpsertSubjectRq, studentId: UUID): SubjectDto {
        val subject = rq.toEntity()
        return subjectDao.save(modifySubject(subject, studentId)).toDto()
    }

    private fun updateSubject(
        id: UUID,
        rq: UpsertSubjectRq,
        studentId: UUID
    ): SubjectDto {
        val subject = getSubject(id).update(rq)
        return subjectDao.save(modifySubject(subject, studentId)).toDto()
    }

    private fun modifySubject(subject: Subject, studentId: UUID): Subject {
        if (studentId !in subject.students.map { it.id }) {
            val student = studentService.getStudent(studentId)
            subject.students.add(student)
            student.subjects.add(subject)
        }
        return subject
    }
}