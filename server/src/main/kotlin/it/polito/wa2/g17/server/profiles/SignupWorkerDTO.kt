package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignupWorkerDTO(
    @field:Email(message = "Email field is not valid")
    @field:NotBlank(message = "Email field is required")
    val email: String,

    @field:NotBlank(message = "Name field is required")
    val name: String,

    @field:NotBlank(message = "Surname field is required")
    val surname: String,

    @field:NotBlank(message = "Role field is required")
    @field:Pattern(regexp = "^(EXPERT|CASHIER)$", message = "Role must be either EXPERT or CASHIER")
    val role: String,

    @field:NotBlank(message = "Password field is required")
    @field:Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @field:Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$", message = "Password must contain at least one lowercase letter, one uppercase letter, one digit and one special character")
    val password: String,
    val skills: List<ProblemType> = emptyList()
)

fun SignupWorkerDTO.toEntity(): Profile {
    return Profile(
        email = this.email,
        name = this.name,
        surname = this.surname,
        role = this.role,
        skills = this.skills.toMutableList(),
        password = this.password
    )
}
