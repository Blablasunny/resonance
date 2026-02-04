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
    var firstName: String,
    @Column(name = "last_name", nullable = false)
    var lastName: String,
    @Column(name = "middle_name")
    var middleName: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    var gender: Gender,
    @Column(name = "birth_date", nullable = false)
    var birthDate: LocalDate,
    @Column(name = "citizenship", nullable = false)
    var citizenship: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "profession_grade", nullable = false)
    var professionGrade: ProfessionGrade
) : AbstractEntity() {
    @ManyToMany(mappedBy = "students")
    var educations: MutableSet<Education> = mutableSetOf()

    @ManyToMany(mappedBy = "students")
    var achievements: MutableSet<Achievement> = mutableSetOf()
}

enum class Gender {
    FEMALE, MALE
}

enum class ProfessionGrade {
    INTERN, JUNIOR, MIDDLE, SENIOR, TEAMLEAD
}