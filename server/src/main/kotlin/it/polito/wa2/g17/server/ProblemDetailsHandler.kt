package it.polito.wa2.g17.server

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import it.polito.wa2.g17.server.products.ProductNotFoundException
import it.polito.wa2.g17.server.profiles.DuplicateProfileException
import it.polito.wa2.g17.server.profiles.ProfileNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity

@RestControllerAdvice
class ProblemDetailsHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(e: ProductNotFoundException) =
        ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )
    @ExceptionHandler(DuplicateProfileException::class)
    fun handleDuplicateProfile(e: DuplicateProfileException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,  e.message!! )

    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFound(e: ProfileNotFoundException) =
        ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(ex: ConstraintViolationException): ResponseEntity<String> {
        println("aaa")
        val errors = ex.constraintViolations.joinToString("; ") {
            if (it.propertyPath.last().name == "email") {
                it.messageTemplate
            } else {
                it.message
            }
        }
        return ResponseEntity.badRequest().body(errors)
    }

}