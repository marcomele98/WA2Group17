package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/API/signup")
class SignupController(private val profileService: ProfileService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody request: SignupCustomerDTO, br: BindingResult) {
        profileService.createCustomer(request)
    }

    //TODO: prevedo cambio password? (soprattutto per expert e cashier per cui la sceglie il manager)
    // potrebbe essere che loro creano i loro profili e poi il manager li approva
    //TODO: password dimenticata?
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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createWorker(@Valid @RequestBody request: SignupWorkerDTO, br: BindingResult) {
        profileService.createWorker(request)
    }



    @GetMapping("{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable email: String): ProfileDTO {
        return profileService.getWorker(email)
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