package it.polito.wa2.g17.server.profiles

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository): ProfileService {
    override fun getProfile(email: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(email)?.toDTO()
    }

    @Transactional
    override fun addProfile(profile: ProfileDTO): ProfileDTO? {
        if(profileRepository.findByIdOrNull(profile.email) != null){
            println("errore")
            return null
            TODO()
        }
        profileRepository.save(profile)
    }

}