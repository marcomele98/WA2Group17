package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

public interface ProfileRepository {
    fun findByEmail(email: String): Profile?
    fun createProfile(profile: Profile)
    fun deleteProfile(email: String)
    fun updateProfile(profile: Profile)
    fun findByRole(role: String): List<Profile>
    fun addRole(email: String, role: String)
    fun removeRole(email: String, role: String)
    fun resetPassword(email: String, newPassword: String, oldPassword: String, confirmPassword: String)
    fun logout(email: String)
}