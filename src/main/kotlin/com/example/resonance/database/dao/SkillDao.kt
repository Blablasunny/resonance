package com.example.resonance.database.dao

import com.example.resonance.database.entity.Skill
import com.example.resonance.database.entity.Student
import com.example.resonance.database.entity.Vacancy

interface SkillDao: AbstractDao<Skill> {
    fun findSkillsByStudents(students: Student): MutableList<Skill>
    fun findSkillsByVacancies(vacancy: Vacancy): MutableList<Skill>
}