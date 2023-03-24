package it.polito.wa2.g17.server.profiles

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/{email}")
    fun getProfile(@PathVariable email: String) : ProfileDTO {
        return profileService.getProfile(email)
    }

    @PostMapping("/profiles")
    fun addProfile(@RequestBody profile: ProfileDTO) : ProfileDTO {
        return profileService.addProfile(profile)
    }

    @PutMapping("/profiles/{email}")
    fun editProfile(@PathVariable email: String, @RequestBody profile: ProfileDTO) : ProfileDTO {
        println("aaa")
        return profileService.editProfile(email, profile)
    }



}