package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import org.keycloak.OAuth2Constants.*
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class ProfileServiceImpl(private val profileRepository: ProfileRepository) : ProfileService {
    override fun getProfile(email: String): ProfileDTO {
        val profile = profileRepository
            .findByIdOrNull(email)
            ?: throw ProfileNotFoundException("Profile with email $email not found")
        return profile.toDTO()
    }

    override fun addProfile(profile: ProfileDTO): ProfileDTO {
        if (profileRepository.findByIdOrNull(profile.email) != null) {
            throw DuplicateProfileException("Profile with email ${profile.email} already exists")
        }
        return profileRepository.save(Profile().apply {
            email = profile.email
            name = profile.name
            surname = profile.surname
            skills = profile.skills.toMutableList()
        }).toDTO()
    }

    @Transactional
    override fun editProfile(email: String, profile: ProfileDTO): ProfileDTO {
        if (profileRepository.findByIdOrNull(email) == null) {
            throw ProfileNotFoundException("Profile with email $email not found")
        }

        if (profile.email != email) {
            throw CannotUpdateEmailException("Impossible to update mail")
        }

        return profileRepository.save(Profile().apply {
            this.email = email
            name = profile.name
            surname = profile.surname
        }).toDTO()
    }

    override fun getProfilesBySkill(skill: ProblemType): List<ProfileDTO> {
        return profileRepository.findBySkills(skill).map { it.toDTO() }
    }

    @Value("\${keycloak.server.url}")
    private lateinit var KEYCLOAK_SERVER_URL: String
    private val REALM_NAME = "WA2G17"

    override fun createCustomer(request: SignupDTO): ProfileDTO {
        return when (createUserOnKeycloak(request, "APP_CLIENT")) {
            201 -> profileRepository.save(Profile().apply {
                email = request.email
                name = request.name
                surname = request.surname
            }).toDTO()

            409 -> throw DuplicateProfileException("Profile with email ${request.email} already exists")
            else -> throw InternalError("Error creating user")
        }
    }

    override fun createExpert(request: SignupExpertDTO): ProfileDTO {
        return when (createUserOnKeycloak(request.toSignupDTO(), "APP_EXPERT")) {
            201 -> profileRepository.save(Profile().apply {
                email = request.email
                name = request.name
                surname = request.surname
                skills = request.skills.toMutableList()
            }).toDTO()

            409 -> throw DuplicateProfileException("Profile with email ${request.email} already exists")
            else -> throw InternalError("Error creating user")
        }
    }

    override fun createCashier(request: SignupDTO): ProfileDTO {
        return when (createUserOnKeycloak(request, "APP_CASHIER")) {
            201 -> profileRepository.save(Profile().apply {
                email = request.email
                name = request.name
                surname = request.surname
            }).toDTO()

            409 -> throw DuplicateProfileException("Profile with email ${request.email} already exists")
            else -> throw InternalError("Error creating user")
        }
    }


    private fun createUserOnKeycloak(request: SignupDTO, role: String): Int {
        val keycloak: Keycloak = KeycloakBuilder
            .builder()
            .serverUrl(KEYCLOAK_SERVER_URL)
            .realm(REALM_NAME)
            .username("admin")
            .password("password")
            .clientId("admin-cli")
            .build()

        println(keycloak.realm(REALM_NAME))

        // Create a UserRepresentation object and set user details
        val user = UserRepresentation()
        user.username = request.email
        user.email = request.email
        user.firstName = request.name
        user.lastName = request.surname
        user.realmRoles = listOf(role)
        user.isEmailVerified = true
        user.isEnabled = true

        // Create a CredentialRepresentation object and set the password
        val password = CredentialRepresentation()
        password.type = CredentialRepresentation.PASSWORD
        password.value = request.password
        password.isTemporary = false

        user.credentials = listOf(password)

        println(user.credentials)

        // Use the Keycloak admin client to create the user
        return keycloak.realm(REALM_NAME).users().create(user).status
    }
}