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
@Table(name = "social_profile")
data class SocialProfile(
    @Column(name = "platform_name", nullable = false)
    var platformName: String,
    @Lob
    @Column(name = "platform_link", nullable = false)
    var platformLink: String,
    @Lob
    @Column(name = "description")
    var description: String?,
): AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "student_socials",
        joinColumns = [JoinColumn(name = "social_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")])
    var students: MutableSet<Student> = mutableSetOf()

    @ManyToMany
    @JoinTable(
        name = "company_socials",
        joinColumns = [JoinColumn(name = "social_id")],
        inverseJoinColumns = [JoinColumn(name = "company_id")])
    var companies: MutableSet<Company> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        students.toList().forEach { it.socialProfiles.remove(this) }
        companies.toList().forEach { it.socialProfiles.remove(this) }
    }
}
