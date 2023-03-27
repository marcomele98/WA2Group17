package it.polito.wa2.g17.server.profiles

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/API/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable email: String): ProfileDTO {
        return profileService.getProfile(email)
    }

    @PostMapping("/API/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProfile(@Valid @RequestBody profile: ProfileDTO, br: BindingResult): ProfileDTO {
        return profileService.addProfile(profile)
    }

    @PutMapping("/API/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun editProfile(
        @PathVariable email: String,
        @Valid @RequestBody profile: ProfileDTO,
        br: BindingResult
    ): ProfileDTO {
        return profileService.editProfile(email, profile)
    }

}