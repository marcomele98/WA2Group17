package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@Validated
@RequestMapping("/API/signup")
class SignupController(private val profileService: ProfileService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody request: SignupCustomerDTO, br: BindingResult): ProfileDTO {
        return profileService.createCustomer(request)
    }
}

@RestController
@Validated
@RequestMapping("/API/reset-password")
class ResetPasswordController(private val profileService: ProfileService) {
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun resetPassword(@Valid @RequestBody request: ResetPasswordDTO, br: BindingResult) {
        profileService.resetPassword(request.email, request.newPassword, request.oldPassword, request.confirmPassword)
    }
}

@RestController
@Validated
@RequestMapping("/API/manager/profiles")
class ManagerProfileController(private val profileService: ProfileService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getWorkers(): List<ProfileDTO> {
        return profileService.getWorkers()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createWorker(@Valid @RequestBody request: SignupWorkerDTO, br: BindingResult): ProfileDTO {
        return profileService.createWorker(request)
    }

    @DeleteMapping("{email}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteWorker(@PathVariable email: String) {
        profileService.deleteWorker(email)
    }

    @GetMapping("{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable email: String): ProfileDTO {
        return profileService.getWorker(email)
    }

    @PutMapping("{email}")
    @ResponseStatus(HttpStatus.OK)
    fun editWorker(
        @PathVariable email: String,
        @Valid @RequestBody request: EditWorkerDTO,
        br: BindingResult
    ): ProfileDTO {
        return profileService.editWorker(email, request)
    }

}

@RestController
@Validated
@RequestMapping("/API/cashier/profiles")
class CashierProfileController(private val profileService: ProfileService) {

    @GetMapping("{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable email: String): ProfileDTO {
        return profileService.getClient(email)
    }

}