package it.polito.wa2.g17.server.profiles

import org.keycloak.OAuth2Constants.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class ProfileServiceImpl(private val profileRepository: ProfileRepository) : ProfileService {
    override fun getProfile(email: String): ProfileDTO {
        val profile = profileRepository
            .findByEmail(email)
            ?: throw ProfileNotFoundException("Profile with email $email not found")
        return profile.toDTO()
    }

    @Transactional
    override fun editProfile(email: String, request: EditWorkerDTO): ProfileDTO {

        if (profileRepository.findByEmail(email) == null) {
            throw ProfileNotFoundException("Profile with email $email not found")
        }

        profileRepository.updateProfile(
            Profile(
                email = email,
                name = request.name,
                surname = request.surname,
                role = request.role,
                skills = request.skills.toMutableList(),
                password = request.password
            )
        )
        return profileRepository.findByEmail(email)!!.toDTO()
    }

    override fun createCustomer(request: SignupCustomerDTO): ProfileDTO {
        profileRepository.createProfile(request.toEntity())
        return profileRepository.findByEmail(request.email)!!.toDTO()
    }

    override fun createWorker(request: SignupWorkerDTO): ProfileDTO {

        if (request.role == "CASHIER" && request.skills.isNotEmpty()) {
            throw InvalidRoleException("Cashier cannot have skills")
        }
        if (request.role == "EXPERT" && request.skills.isEmpty()) {
            throw InvalidRoleException("Expert must have at least one skill")
        }
        profileRepository.createProfile(request.toEntity())
        return profileRepository.findByEmail(request.email)!!.toDTO()
    }

    override fun getWorker(email: String): ProfileDTO {
        val profile = profileRepository
            .findByEmail(email)
            ?: throw ProfileNotFoundException("Profile with email $email not found")
        if (profile.role != "EXPERT" && profile.role != "CASHIER" && profile.role != "MANAGER") {
            throw ProfileNotFoundException("Profile with email $email not found with role EXPERT, CASHIER or MANAGER")
        }
        return profile.toDTO()
    }

    override fun getClient(email: String): ProfileDTO {
        val profile = profileRepository
            .findByEmail(email)
            ?: throw ProfileNotFoundException("Profile with email $email not found")
        if (profile.role != "CUSTOMER") {
            throw ProfileNotFoundException("Profile with email $email not found")
        }
        return profile.toDTO()
    }

    override fun getWorkers(): List<ProfileDTO> {
        val experts = profileRepository.findByRole("EXPERT")
        val cashiers = profileRepository.findByRole("CASHIER")
        return (experts + cashiers).map { it.toDTO() }
    }


}