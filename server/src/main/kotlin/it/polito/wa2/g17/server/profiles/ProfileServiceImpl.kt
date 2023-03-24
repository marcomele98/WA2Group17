package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.products.ProductNotFoundException
import it.polito.wa2.g17.server.products.toDTO
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository) : ProfileService {
    override fun getProfile(email: String): ProfileDTO {
        val profile = profileRepository
            .findByIdOrNull(email)
            ?: throw ProfileNotFoundException()
        return profile.toDTO()
    }

    @Transactional
    override fun addProfile(profile: ProfileDTO): ProfileDTO {
        if (profileRepository.findByIdOrNull(profile.email) != null) {
            throw DuplicateProfileException()
        }
        return profileRepository.save(Profile().apply {
            email = profile.email
            name = profile.name
            surname = profile.surname
        }).toDTO()
    }

    @Transactional
    override fun editProfile(email: String, profile: ProfileDTO): ProfileDTO {
        if (profileRepository.findByIdOrNull(email) == null) {
            throw ProfileNotFoundException()
        }
        return profileRepository.save(Profile().apply {
            this.email = email
            name = profile.name
            surname = profile.surname
        }).toDTO()
    }

}