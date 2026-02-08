package com.example.resonance.database.dao

import com.example.resonance.database.entity.Skill
import com.example.resonance.database.entity.Student

interface SkillDao: AbstractDao<Skill> {
    fun findSkillByStudents(students: Student): MutableList<Skill>
}