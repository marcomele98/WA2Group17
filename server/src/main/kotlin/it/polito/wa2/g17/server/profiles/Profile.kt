package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import org.keycloak.representations.idm.UserRepresentation

data class Profile(
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val role: String? = null,
    val password: String? = null,
    val skills: List<ProblemType> = listOf()
)

fun Profile.toUserRepresentation(): UserRepresentation{
    val user = UserRepresentation()
    user.username = this.email
    user.email = this.email
    user.firstName = this.name
    user.lastName = this.surname
    user.isEmailVerified = true
    user.isEnabled = true
    user.attributes = mapOf("skills" to this.skills.map { it.name })
    // Create a UserRepresentation object and set user details
    return user
}

fun UserRepresentation.toProfile(role: String): Profile {
    return Profile(
        email = this.email,
        name = this.firstName,
        surname = this.lastName,
        skills = this.attributes?.getOrDefault("skills", emptyList())?.map { ProblemType.valueOf(it) } ?: listOf(),
        role = role
    )
}

