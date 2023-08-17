package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.springframework.beans.factory.annotation.Value
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.stereotype.Repository

@Repository
public class ProfileRepositoryImpl : ProfileRepository {

    @Value("\${keycloak.server.url}")
    private lateinit var KEYCLOAK_SERVER_URL: String
    private val REALM_NAME = "WA2G17"
    override fun findByEmail(email: String): Profile? {
        val keycloak: Keycloak = keycloakAdmin()
        val id = keycloak.realm("WA2G17").users().search(email).firstOrNull()?.id
            ?: return null
        val userResource = keycloak.realm("WA2G17").users()[id]
        val roles: List<String> = userResource.roles().realmLevel().listEffective().filter { it.name.contains("APP_") }
            .map { it.name.replace("APP_", "") }

        val user = userResource.toRepresentation()

        val role = roles.firstOrNull() ?: throw Exception("User has no role")

        return user.toProfile(role)
    }

    /*override fun findBySkills(skill: ProblemType): List<Profile> {
        return getAllByRole("EXPERT").filter { skill in it.skills }
    }*/

    override fun createProfile(profile: Profile) {
        val keycloak: Keycloak = keycloakAdmin()

        val user = profile.toUserRepresentation()
        // Create a CredentialRepresentation object and set the password
        val credentials = CredentialRepresentation()
        credentials.type = CredentialRepresentation.PASSWORD
        credentials.value = profile.password
        credentials.isTemporary = false
        user.credentials = listOf(credentials)

        when (keycloak.realm(REALM_NAME).users().create(user).status) {
            201 -> return
            409 -> throw Exception("User already exists")
            else -> throw Exception("Error creating user") //TODO: gestire i casi di errore
        }
    }

    override fun deleteProfile(email: String) {
        val keycloak: Keycloak = keycloakAdmin()
        val id = keycloak.realm("WA2G17").users().search(email).firstOrNull()?.id
            ?: throw Exception("User not found")
        keycloak.realm("WA2G17").users()[id].remove()
    }


    override fun addRole(email: String, role: String) {
        val keycloak: Keycloak = keycloakAdmin()
        val id = keycloak.realm("WA2G17").users().search(email).firstOrNull()?.id
            ?: throw Exception("User not found")
        val userResource = keycloak.realm("WA2G17").users()[id]
        userResource.roles().realmLevel().add(listOf(keycloak.realm("WA2G17").roles()["APP_$role"].toRepresentation()))
    }

    override fun removeRole(email: String, role: String) {
        val keycloak: Keycloak = keycloakAdmin()
        val id = keycloak.realm("WA2G17").users().search(email).firstOrNull()?.id
            ?: throw Exception("User not found")
        val userResource = keycloak.realm("WA2G17").users()[id]
        userResource.roles().realmLevel().remove(listOf(keycloak.realm("WA2G17").roles()["APP_$role"].toRepresentation()))
    }


    override fun updateProfile(profile: Profile) {
        val keycloak: Keycloak = keycloakAdmin()

        // Create a UserRepresentation object and set user details
        val user = profile.toUserRepresentation()

        // Create a CredentialRepresentation object and set the password
        if (profile.password != null) {
            val credentials = CredentialRepresentation()
            credentials.type = CredentialRepresentation.PASSWORD
            credentials.value = profile.password
            credentials.isTemporary = false
            user.credentials = listOf(credentials)
        }

        val id = keycloak.realm("WA2G17").users().search(profile.email).firstOrNull()?.id
            ?: throw Exception("User not found")

        keycloak.realm(REALM_NAME).users()[id].update(user)
    }


    private fun keycloakAdmin(): Keycloak {
        return KeycloakBuilder
            .builder()
            .serverUrl(KEYCLOAK_SERVER_URL)
            .realm(REALM_NAME)
            .username("admin")
            .password("password")
            .clientId("admin-cli")
            .build()
    }

    override fun findByRole(role: String): List<Profile> {
        val keycloak: Keycloak = keycloakAdmin()
        return keycloak.realm("WA2G17").roles()["APP_$role"].userMembers.map {
            Profile(
                email = it.email,
                name = it.firstName,
                surname = it.lastName,
                skills = it.attributes?.getOrDefault("skills", listOf())?.map { ProblemType.valueOf(it) }
                    ?: emptyList(),
                role = role
            )
        }
    }

    override fun resetPassword(email: String, newPassword: String, oldPassword: String, confirmPassword: String) {
        val keycloak: Keycloak = keycloakAdmin()
        val id = keycloak.realm("WA2G17").users().search(email).firstOrNull()?.id
            ?: throw Exception("User not found")
        val userResource = keycloak.realm("WA2G17").users()[id]
        val credentials = CredentialRepresentation()
        credentials.type = CredentialRepresentation.PASSWORD
        credentials.value = newPassword
        credentials.isTemporary = false
        userResource.resetPassword(credentials)

    }

    override fun logout(email: String) {
        val keycloak: Keycloak = keycloakAdmin()
        val id = keycloak.realm("WA2G17").users().search(email).firstOrNull()?.id
            ?: throw Exception("User not found")
        val userResource = keycloak.realm("WA2G17").users()[id]
        userResource.logout()
    }

}