package it.polito.wa2.g17.server

import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g17.server.products.ProductNotFoundException
import it.polito.wa2.g17.server.profiles.CannotUpdateEmailException
import it.polito.wa2.g17.server.profiles.DuplicateProfileException
import it.polito.wa2.g17.server.profiles.ProfileNotFoundException
import it.polito.wa2.g17.server.ticketing.tickets.*
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
@Observed
class ProblemDetailsHandler : ResponseEntityExceptionHandler() {

    private val log: Logger = LoggerFactory.getLogger(ProblemDetailsHandler::class.java)

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(e: ProductNotFoundException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(DuplicateProfileException::class)
    fun handleDuplicateProfile(e: DuplicateProfileException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)
    }

    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFound(e: ProfileNotFoundException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(ex: ConstraintViolationException): ProblemDetail {
        val errors = ex.constraintViolations.joinToString("; ") {
                it.messageTemplate
        }
        log.error(ex.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, errors!!)
    }

    @ExceptionHandler(CannotUpdateEmailException::class)
    fun handleEmailException(e: CannotUpdateEmailException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)
    }

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFound(e: TicketNotFoundException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(WrongStateException::class)
    fun handleWrongState(e: WrongStateException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }

    @ExceptionHandler(WrongSkillsException::class)
    fun handleWrongSkills(e: WrongSkillsException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)
    }

    @ExceptionHandler(WrongUserException::class)
    fun handleWrongUser(e: WrongUserException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.message!!)
    }

    @ExceptionHandler(WrongAttachmentsException::class)
    fun handleWrongAttachments(e: WrongAttachmentsException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)
    }

    @ExceptionHandler(AttachmentNotFoundException::class)
    fun handleAttachmentsNotFound(e: AttachmentNotFoundException) {
        log.error(e.toString())
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

}