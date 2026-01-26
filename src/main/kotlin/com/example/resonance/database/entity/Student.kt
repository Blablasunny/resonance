package com.example.resonance.database.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "student")
data class Student(
    @Column(name = "first_name", nullable = false)
    val firstName: String,
    @Column(name = "last_name", nullable = false)
    val lastName: String,
    @Column(name = "middle_name")
    val middleName: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    val gender: Gender,
    @Column(name = "birth_date", nullable = false)
    val birthDate: LocalDate,
    @Column(name = "citizenship", nullable = false)
    val citizenship: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "profession_grade", nullable = false)
    val professionGrade: ProfessionGrade
) : AbstractEntity() {
    @ManyToMany(mappedBy = "students")
    var educations: MutableSet<Education> = mutableSetOf()
}

enum class Gender {
    FEMALE, MALE
}

enum class ProfessionGrade {
    INTERN, JUNIOR, MIDDLE, SENIOR, TEAMLEAD
}