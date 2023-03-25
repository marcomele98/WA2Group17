package it.polito.wa2.g17.server.profiles

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class ProfileDTO(

    @field:NotBlank(message = "Email field is required")
    @field:Email(message = "Email field is not valid")
    val email: String,

    @field:Size(min = 1, max = 127, message = "Name field must be between 1 and 127 characters")
    @field:NotBlank(message = "Name field is required")
    val name: String,

    @field:Size(min = 1, max = 127, message = "Surname field must be between 1 and 127 characters")
    @field:NotBlank(message = "Surname field is required")
    val surname: String
)

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(email, name, surname)
}