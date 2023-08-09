package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
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

    @PutMapping("message/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun addMessage(@PathVariable ticketId: Long, @Valid @RequestBody message: MessageDTO, principal: Authentication, br: BindingResult): CompleteTicketDTO {
        return ticketService.addMessage(ticketId, message, principal.name, principal.authorities.map{ it.authority }.toList()[0].toString() )
    }

    @PutMapping("close/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun closeTicket(@PathVariable ticketId: Long, principal: Authentication): CompleteTicketDTO {
        return ticketService.closeTicket(ticketId, principal.name, principal.authorities.map{ it.authority }.toList()[0].toString())
    }
}


@RestController
@Validated
@RequestMapping("/API/manager/tickets")
class TicketManagerController(private val ticketService: TicketService) {
    @GetMapping("open")
    @ResponseStatus(HttpStatus.OK)
    fun getAllOpen(): List<PartialTicketDTO> {
        return ticketService.getAllOpen()
    }

    @GetMapping("assigned")
    @ResponseStatus(HttpStatus.OK)
    fun getAllAssigned(): List<PartialTicketDTO> {
        return ticketService.getAllAssigned()
    }

    @GetMapping("statusHistory/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getStatusHistory(@PathVariable ticketId: Long): List<StatusChangeDTO> {
        return ticketService.getStatusHistory(ticketId)
    }

    @PutMapping("assign/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun assignTicket(@PathVariable ticketId: Long, @Valid @RequestBody assignTicketDTO: AssignTicketDTO, br: BindingResult): CompleteTicketDTO {
        return ticketService.assignTicket(ticketId, assignTicketDTO.expertEmail, assignTicketDTO.priority)
    }

}


@RestController
@Validated
@RequestMapping("/API/expert/tickets")
class TicketExpertController(private val ticketService: TicketService) {
    @GetMapping("unresolved")
    @ResponseStatus(HttpStatus.OK)
    fun getAssignedByExpertId(principal: Authentication): List<PartialTicketDTO> {
        return ticketService.getUnresolvedByExpertEmail(principal.name)
    }


    @GetMapping("resolved")
    @ResponseStatus(HttpStatus.OK)
    fun getResolvedByExpertId(principal: Authentication): List<PartialTicketDTO> {
        return ticketService.getResolvedByExpertEmail(principal.name)
    }

    @PutMapping("resolve/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun resolveTicket(@PathVariable ticketId: Long, principal: Authentication): CompleteTicketDTO {
        return ticketService.resolveTicket(ticketId, principal.name)
    }
}

@RestController
@Validated
@RequestMapping("/API/customer/tickets")
class TicketCustomerController(private val ticketService: TicketService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addTicket(@Valid @RequestBody ticket: CreateTicketDTO, br: BindingResult,  principal: Authentication): CompleteTicketDTO {
        return ticketService.createTicket(ticket, principal.name)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllByCustomerId(principal: Authentication): List<PartialTicketDTO> {
        return ticketService.getAllByCustomerEmail(principal.name)
    }

    @PutMapping("reopen/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun reopenTicket(@PathVariable ticketId: Long, principal: Authentication): CompleteTicketDTO {
        return ticketService.reopenTicket(ticketId, principal.name)
    }
}