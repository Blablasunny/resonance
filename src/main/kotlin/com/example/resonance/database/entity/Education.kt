package com.example.resonance.database.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table

@Entity
@Table(name = "education")
data class Education(
    @Column(name = "institution_name", nullable = false)
    var institutionName: String,
    @Column(name = "education_level", nullable = false)
    var educationLevel: EducationLevel,
    @Column(name = "speciality", nullable = false)
    var speciality: String,
    @Column(name = "status", nullable = false)
    var isFinished: Boolean = true,
) : AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "student_education",
        joinColumns = [JoinColumn(name = "education_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")])
    var students: MutableSet<Student> = mutableSetOf()

    @PreRemove
    private fun preRemove() {
        val studentsCopy = students.toList()
        studentsCopy.forEach { it.educations.remove(this) }
        students.clear()
    }
}

enum class EducationLevel {
    BACHELOR,     // Бакалавриат
    MASTER,       // Магистратура
    SPECIALIST,   // Специалитет
    POSTGRADUATE, // Аспирантура
}