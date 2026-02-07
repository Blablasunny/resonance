package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table

@Entity
@Table(name = "occupation_of_interest")
data class OccupationOfInterest(
    @Column(name = "occupation_name", nullable = false)
    var occupationName: String,
): AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "student_occupation_interests",
        joinColumns = [JoinColumn(name = "occupation_interest_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")])
    var students: MutableSet<Student> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        students.toList().forEach { it.occupationOfInterests.remove(this) }
    }
}
