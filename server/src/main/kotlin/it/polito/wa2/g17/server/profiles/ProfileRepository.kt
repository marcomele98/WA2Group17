package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

public interface ProfileRepository {
    fun findByEmail(email: String): Profile?
    fun createProfile(profile: Profile)
    fun updateProfile(profile: Profile)
    fun findByRole(role: String): List<Profile>
}