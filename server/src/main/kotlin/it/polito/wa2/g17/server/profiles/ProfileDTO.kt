package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.ProblemType
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ProfileDTO(

    @field:Email(message = "Email field is not valid")
    @field:NotBlank(message = "Email field is required")
    val email: String,

    @field:NotBlank(message = "Name field is required")
    val name: String,

    @field:NotBlank(message = "Surname field is required")
    val surname: String,

    val skills: List<ProblemType> = listOf()
)

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(email, name, surname)
}