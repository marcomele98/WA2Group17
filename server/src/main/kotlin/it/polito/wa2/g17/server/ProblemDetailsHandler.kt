package it.polito.wa2.g17.server

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import it.polito.wa2.g17.server.products.ProductNotFoundException
import it.polito.wa2.g17.server.profiles.CannotUpdateEmailException
import it.polito.wa2.g17.server.profiles.DuplicateProfileException
import it.polito.wa2.g17.server.profiles.ProfileNotFoundException
import it.polito.wa2.g17.server.ticketing.tickets.*
import jakarta.servlet.ServletException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

@RestControllerAdvice
class ProblemDetailsHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(e: ProductNotFoundException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(DuplicateProfileException::class)
    fun handleDuplicateProfile(e: DuplicateProfileException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFound(e: ProfileNotFoundException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(ex: ConstraintViolationException): ProblemDetail {
        val errors = ex.constraintViolations.joinToString("; ") {
                it.messageTemplate
        }
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, errors!!)
    }

    @ExceptionHandler(CannotUpdateEmailException::class)
    fun handleEmailException(e: CannotUpdateEmailException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFound(e: TicketNotFoundException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(WrongStateException::class)
    fun handleWrongState(e: WrongStateException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)

    @ExceptionHandler(WrongSkillsException::class)
    fun handleWrongSkills(e: WrongSkillsException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)

    @ExceptionHandler(WrongUserException::class)
    fun handleWrongUser(e: WrongUserException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.message!!)

    @ExceptionHandler(WrongAttachmentsException::class)
    fun handleWrongAttachments(e: WrongAttachmentsException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)

    @ExceptionHandler(AttachmentNotFoundException::class)
    fun handleAttachmentsNotFound(e: AttachmentNotFoundException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)


}