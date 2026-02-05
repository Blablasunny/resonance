package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "experience")
data class Experience(
    @Column(name = "company_name", nullable = false)
    var companyName: String,
    @Column(name = "position", nullable = false)
    var position: String,
    @Column(name = "start_date", nullable = false)
    var startDate: LocalDate,
    @Column(name = "end_date")
    var endDate: LocalDate?,
) : AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "work_experiences",
        joinColumns = [JoinColumn(name = "experience_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")])
    var students: MutableSet<Student> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        students.toList().forEach { it.experiences.remove(this) }
        students.clear()
    }
}
