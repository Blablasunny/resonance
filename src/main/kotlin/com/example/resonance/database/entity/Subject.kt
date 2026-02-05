package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table

@Entity
@Table(name = "subject")
data class Subject(
    @Column(name = "subject_name", nullable = false)
    var subjectName: String,
): AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "student_subjects",
        joinColumns = [JoinColumn(name = "subject_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")])
    var students: MutableSet<Student> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        students.toList().forEach { it.subjects.remove(this) }
        students.clear()
    }
}
