package it.polito.wa2.g17.server.profiles

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository) : ProfileService {
    override fun getProfile(email: String): ProfileDTO {
        val profile = profileRepository
            .findByIdOrNull(email)
            ?: throw ProfileNotFoundException("Profile with email $email not found")
        return profile.toDTO()
    }

    @Transactional
    override fun addProfile(profile: ProfileDTO): ProfileDTO {
        if (profileRepository.findByIdOrNull(profile.email) != null) {
            throw DuplicateProfileException("Profile with email ${profile.email} already exists")
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
            throw ProfileNotFoundException("Profile with email $email not found")
        }
        return profileRepository.save(Profile().apply {
            this.email = email
            name = profile.name
            surname = profile.surname
        }).toDTO()
    }

}