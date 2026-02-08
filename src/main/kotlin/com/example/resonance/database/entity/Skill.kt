package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table

@Entity
@Table(name = "skill")
data class Skill(
    @Column(name = "skill_name")
    var skillName: String,
    @Column(name = "skill_category")
    var skillCategory: String,
): AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "student_skills",
        joinColumns = [JoinColumn(name = "skill_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")])
    var students: MutableSet<Student> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        students.toList().forEach { it.skills.remove(this) }
    }
}
