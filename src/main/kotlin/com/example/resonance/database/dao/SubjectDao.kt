package com.example.resonance.database.dao

import com.example.resonance.database.entity.Student
import com.example.resonance.database.entity.Subject

interface SubjectDao: AbstractDao<Subject> {
    fun findSubjectsByStudents(students: Student): MutableList<Subject>
}