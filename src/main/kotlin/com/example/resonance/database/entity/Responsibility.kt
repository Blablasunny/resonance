package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.Lob
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table

@Entity
@Table(name = "responsibility")
data class Responsibility(
    @Column(name = "responsibility_name", nullable = false)
    var responsibilityName: String,
    @Lob
    @Column(name = "description", nullable = false)
    var description: String,
): AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "responsibilities",
        joinColumns = [JoinColumn(name = "responsibility_id")],
        inverseJoinColumns = [JoinColumn(name = "vacancy_id")])
    var vacancies: MutableSet<Vacancy> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        vacancies.toList().forEach { it.responsibilities.remove(this) }
    }
}
