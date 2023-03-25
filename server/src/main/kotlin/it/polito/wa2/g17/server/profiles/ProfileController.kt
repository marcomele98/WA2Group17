package it.polito.wa2.g17.server.profiles

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/{email}")
    fun getProfile(@PathVariable email: String) : ProfileDTO {
        return profileService.getProfile(email)
    }

    @PostMapping("/profiles")
    fun addProfile(@Valid @RequestBody profile: ProfileDTO) : ProfileDTO {
        return profileService.addProfile(profile)
    }

    @PutMapping("/profiles/{email}")
    fun editProfile(@PathVariable email: String, @Valid @RequestBody profile: ProfileDTO) : ProfileDTO {
        return profileService.editProfile(email, profile)
    }



}