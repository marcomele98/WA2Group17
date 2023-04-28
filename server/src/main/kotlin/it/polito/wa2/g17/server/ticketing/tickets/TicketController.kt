package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import jakarta.persistence.Id
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/API/tickets")
class TicketGeneralController(private val ticketService: TicketService) {
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getTicket(@PathVariable id: Long): CompleteTicketDTO {
        return ticketService.getTicket(id)
    }
}


@RestController
@Validated
@RequestMapping("/API/manager/tickets")
class TicketManagerController(private val ticketService: TicketService) {
    @GetMapping("/open")
    @ResponseStatus(HttpStatus.OK)
    fun getAllOpen(): List<PartialTicketDTO> {
        return ticketService.getAllOpen()
    }

    @GetMapping("/assigned")
    @ResponseStatus(HttpStatus.OK)
    fun getAllAssigned(): List<PartialTicketDTO> {
        return ticketService.getAllAssigned()
    }

    @GetMapping("/statusHistory/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getStatusHistory(@PathVariable ticketId: Long): List<StatusChangeDTO> {
        return ticketService.getStatusHistory(ticketId)
    }

    @PutMapping("/assign/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun assignTicket(@PathVariable ticketId: Long, @Valid @RequestBody expertId: Long): CompleteTicketDTO {
        return ticketService.assignTicket(ticketId, expertId)
    }

}


@RestController
@Validated
@RequestMapping("/API/expert/tickets")
class TicketExpertController(private val ticketService: TicketService) {
    @GetMapping("/unresolved/{expertId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAssignedByExpertId(@PathVariable expertId: Long): List<PartialTicketDTO> {
        return ticketService.getUnresolvedByExpertId(expertId)
    }


    //TODO: qui quando avrò il modulo della sicurezza avrò un Principal da cui mi vado a prendere l'id del customer
    @GetMapping("/resolved/{expertId}")
    @ResponseStatus(HttpStatus.OK)
    fun getResolvedByExpertId(@PathVariable expertId: Long): List<PartialTicketDTO> {
        return ticketService.getResolvedByExpertId(expertId)
    }

    @PutMapping("/resolve/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun resolveTicket(@PathVariable ticketId: Long): CompleteTicketDTO {
        return ticketService.resolveTicket(ticketId)
    }
}

@RestController
@Validated
@RequestMapping("/API/customer/tickets")
class TicketCustomerController(private val ticketService: TicketService) {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun addTicket(@Valid @RequestBody ticket: CreateTicketDTO, br: BindingResult): CompleteTicketDTO {
        return ticketService.createTicket(ticket)
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAllByCustomerId(@PathVariable customerId: Long): List<PartialTicketDTO> {
        return ticketService.getAllByCustomerId(customerId)
    }

    @PutMapping("/reopen/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun reopenTicket(@PathVariable ticketId: Long): CompleteTicketDTO {
        return ticketService.reopenTicket(ticketId)
    }

    @PutMapping("/close/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    //TODO: qui quando avrò il modulo della sicurezza avrò un Principal da cui mi vado a prendere l'id e scopro se è customer o esperto
    //TODO: forse qua sarà necessario il campo @LastModifiedBy
    fun closeTicket(@PathVariable ticketId: Long, @PathVariable userId: Long): CompleteTicketDTO {
        return ticketService.closeTicket(ticketId, userId)
    }

    @PutMapping("/addMessage/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun addMessage(@PathVariable ticketId: Long, @Valid @RequestBody message: MessageDTO): CompleteTicketDTO {
        return ticketService.addMessage(ticketId, message)
    }
}