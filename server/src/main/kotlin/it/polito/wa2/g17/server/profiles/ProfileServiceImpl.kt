package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.security.AuthRepository
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.Ticket
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val ticketRepository: TicketRepository
) :
    ProfileService {
    override fun getProfile(email: String): ProfileDTO {
        val profile = profileRepository
            .findByEmail(email)
            ?: throw ProfileNotFoundException("Profile with email $email not found")
        return profile.toDTO()
    }

    @Transactional
    override fun editWorker(email: String, request: EditWorkerDTO): ProfileDTO {

        if (request.role == "CASHIER" && request.skills.isNotEmpty()) {
            throw InvalidRoleException("Cashier cannot have skills")
        }

        if (request.role == "EXPERT" && request.skills.isEmpty()) {
            throw InvalidRoleException("Expert must have at least one skill")
        }

        val profile = profileRepository.findByEmail(email)
            ?: throw ProfileNotFoundException("Profile with email $email not found")

        if (profile.role !== request.role) {
            ticketRepository.findAllByExpertEmailAndStatusIn(
                email,
                listOf(Status.IN_PROGRESS, Status.CLOSED, Status.RESOLVED)
            ).forEach {
                if (!request.skills.contains(it.problemType)) {
                    it.expertEmail = null
                    if (it.status == Status.IN_PROGRESS) {
                        it.status = Status.OPEN
                    }
                }
                ticketRepository.save(it)
            }
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

        try {
            profileRepository.removeRole(email, profile.role!!)
            profileRepository.addRole(email, request.role)
        } catch (e: Exception) {
            profileRepository.updateProfile(profile)
            throw e
        }

        return profileRepository.findByEmail(email)!!.toDTO()
    }

    override fun createCustomer(request: SignupCustomerDTO): ProfileDTO {
        if (profileRepository.findByEmail(request.email) != null) {
            throw ProfileAlreadyExistsException("Profile with email ${request.email} already exists")
        }
        profileRepository.createProfile(request.toEntity())
        try {
            profileRepository.addRole(request.email, "CLIENT")
        } catch (e: Exception) {
            profileRepository.deleteProfile(request.email)
            throw e
        }

        return profileRepository.findByEmail(request.email)!!.toDTO()

    }

    override fun createWorker(request: SignupWorkerDTO): ProfileDTO {

        if (profileRepository.findByEmail(request.email) != null) {
            throw ProfileAlreadyExistsException("Profile with email ${request.email} already exists")
        }

        if (request.role == "CASHIER" && request.skills.isNotEmpty()) {
            throw InvalidRoleException("Cashier cannot have skills")
        }
        if (request.role == "EXPERT" && request.skills.isEmpty()) {
            throw InvalidRoleException("Expert must have at least one skill")
        }
        profileRepository.createProfile(request.toEntity())
        try {
            profileRepository.addRole(request.email, request.role)
        } catch (e: Exception) {
            profileRepository.deleteProfile(request.email)
            throw e
        }
        return profileRepository.findByEmail(request.email)!!.toDTO()
    }

    override fun deleteWorker(email: String) {
        val profile = profileRepository
            .findByEmail(email)
            ?: throw ProfileNotFoundException("Profile with email $email not found")
        if (profile.role != "EXPERT" && profile.role != "CASHIER") {
            throw ProfileNotFoundException("Profile with email $email not found with role EXPERT or CASHIER")
        }
        ticketRepository.findAllByExpertEmailAndStatusIn(
            email,
            listOf(Status.IN_PROGRESS, Status.CLOSED, Status.RESOLVED)
        ).forEach {
            it.expertEmail = null
            if (it.status == Status.IN_PROGRESS) {
                it.status = Status.OPEN
            }
            ticketRepository.save(it)
        }
        profileRepository.deleteProfile(email)
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
        if (profile.role != "CLIENT") {
            throw ProfileNotFoundException("Profile with email $email not found")
        }
        return profile.toDTO()
    }

    override fun getWorkers(): List<ProfileDTO> {
        val experts = profileRepository.findByRole("EXPERT")
        val cashiers = profileRepository.findByRole("CASHIER")
        return (experts + cashiers).map { it.toDTO() }
    }


    override fun resetPassword(email: String, newPassword: String, oldPassword: String, confirmPassword: String) {
        authRepository.login(email, oldPassword)
        if (newPassword != confirmPassword) {
            throw Exception("Passwords do not match")
        }
        profileRepository.resetPassword(email, newPassword, oldPassword, confirmPassword)
        profileRepository.logout(email)
    }


}