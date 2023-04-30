package it.polito.wa2.g17.server.profiles

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : JpaRepository<Profile, String> {
    fun findBySkills(skill: String): List<Profile>
}