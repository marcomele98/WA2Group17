package it.polito.wa2.g17.server.profiles

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/{email}")
    fun getProfile(@PathVariable email: String) : ProfileDTO? {
        return profileService.getProfile(email)
    }
}