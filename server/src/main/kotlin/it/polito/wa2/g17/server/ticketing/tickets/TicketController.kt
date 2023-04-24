package it.polito.wa2.g17.server.ticketing.tickets

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
class TicketController(private val ticketService: TicketService) {

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getTicket(@PathVariable id: Long): CompleteTicketDTO {
        return ticketService.getTicket(id)
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun addTicket(@Valid @RequestBody ticket: CreateTicketDTO, br: BindingResult): CompleteTicketDTO {
        return ticketService.createTicket(ticket)
    }

    @GetMapping("manager/open")
    @ResponseStatus(HttpStatus.OK)
    fun getAllOpen(): List<PartialTicketDTO> {
        return ticketService.getAllOpen()
    }

    @GetMapping("manager/assigned")
    @ResponseStatus(HttpStatus.OK)
    fun getAllAssigned(): List<PartialTicketDTO> {
        return ticketService.getAllAssigned()
    }

    @GetMapping("expert/unresolved/{expertId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAssignedByExpertId(@PathVariable expertId: Long): List<PartialTicketDTO> {
        return ticketService.getUnresolvedByExpertId(expertId)
    }


    @GetMapping("expert/resolved/{expertId}")
    @ResponseStatus(HttpStatus.OK)
    fun getResolvedByExpertId(@PathVariable expertId: Long): List<PartialTicketDTO> {
        return ticketService.getResolvedByExpertId(expertId)
    }

    @GetMapping("customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAllByCustomerId(@PathVariable customerId: Long): List<PartialTicketDTO> {
        return ticketService.getAllByCustomerId(customerId)
    }

    @GetMapping("manager/statusHistory/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    fun getStatusHistory(@PathVariable ticketId: Long): List<StatusChangeDTO> {
        return ticketService.getStatusHistory(ticketId)
    }


}