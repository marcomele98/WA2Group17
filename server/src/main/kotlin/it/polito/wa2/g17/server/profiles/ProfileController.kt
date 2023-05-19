package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/API/profiles")
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable email: String): ProfileDTO {
        return profileService.getProfile(email)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addProfile(@Valid @RequestBody profile: ProfileDTO, br: BindingResult): ProfileDTO {
        return profileService.addProfile(profile)
    }

    @PutMapping("{email}")
    @ResponseStatus(HttpStatus.OK)
    fun editProfile(
        @PathVariable email: String,
        @Valid @RequestBody profile: ProfileDTO,
        br: BindingResult
    ): ProfileDTO {
        return profileService.editProfile(email, profile)
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody request: SignupDTO, br: BindingResult): ProfileDTO {
        return profileService.createCustomer(request)
    }

}

@RestController
@Validated
@RequestMapping("/API/manager/profiles")
class ManagerProfileController(private val profileService: ProfileService) {
    @GetMapping("{skill}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfilesBySkill(@PathVariable skill: ProblemType): List<ProfileDTO> {
        return profileService.getProfilesBySkill(skill)
    }

    @PostMapping("/create-expert")
    @ResponseStatus(HttpStatus.CREATED)
    fun createExpert(@Valid @RequestBody request: SignupExpertDTO, br: BindingResult): ProfileDTO {
        return profileService.createExpert(request)
    }

}

