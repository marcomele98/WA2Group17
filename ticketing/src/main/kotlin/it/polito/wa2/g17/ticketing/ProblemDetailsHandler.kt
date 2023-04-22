package it.polito.wa2.g17.ticketing

import it.polito.wa2.g17.ticketing.tickets.TicketNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class ProblemDetailsHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleProductNotFound(e: TicketNotFoundException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

}