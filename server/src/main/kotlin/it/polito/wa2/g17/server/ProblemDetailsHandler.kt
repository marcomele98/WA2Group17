package it.polito.wa2.g17.server

import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g17.server.products.ProductNotFoundException
import it.polito.wa2.g17.server.profiles.*
import it.polito.wa2.g17.server.security.UnauthorizedException
import it.polito.wa2.g17.server.ticketing.tickets.*
import it.polito.wa2.g17.server.ticketing.warranties.WarrantyNotFoundException
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

    @ExceptionHandler(WarrantyNotFoundException::class)
    fun handleWarrantyNotFound(e: WarrantyNotFoundException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(ProfileAlreadyExistsException::class)
    fun handleProfileAlreadyExists(e: ProfileAlreadyExistsException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)
    }

    @ExceptionHandler(InvalidRoleException::class)
    fun handleInvalidRole(e: InvalidRoleException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(e: UnauthorizedException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.message!!)
    }

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(e: ProductNotFoundException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(DuplicateProfileException::class)
    fun handleDuplicateProfile(e: DuplicateProfileException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)
    }

    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFound(e: ProfileNotFoundException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
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
    fun handleEmailException(e: CannotUpdateEmailException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)
    }

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFound(e: TicketNotFoundException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(WrongStateException::class)
    fun handleWrongState(e: WrongStateException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }

    @ExceptionHandler(WrongSkillsException::class)
    fun handleWrongSkills(e: WrongSkillsException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)
    }

    @ExceptionHandler(WrongUserException::class)
    fun handleWrongUser(e: WrongUserException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.message!!)
    }

    @ExceptionHandler(WrongAttachmentsException::class)
    fun handleWrongAttachments(e: WrongAttachmentsException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.message!!)
    }

    @ExceptionHandler(AttachmentNotFoundException::class)
    fun handleAttachmentsNotFound(e: AttachmentNotFoundException): ProblemDetail {
        log.error(e.toString())
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

}