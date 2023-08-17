package it.polito.wa2.g17.server.profiles

class ResetPasswordDTO (
    val email: String,
    val newPassword: String,
    val confirmPassword: String,
    val oldPassword: String
)
