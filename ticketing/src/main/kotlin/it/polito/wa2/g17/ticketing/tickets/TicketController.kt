package it.polito.wa2.g17.ticketing.tickets

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/API/tickets")
class TicketController(private val ticketService: TicketService) {

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getTicket(@PathVariable id: Long): TicketDTO {
        return ticketService.getTicket(id)
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun addTicket(@Validated @RequestBody ticket: TicketDTO): TicketDTO {
        return ticketService.createTicket(ticket)
    }

    @GetMapping("open")
    @ResponseStatus(HttpStatus.OK)
    fun getOpen(): List<TicketDTO> {
        return ticketService.getOpen()
    }

    @GetMapping("assigned")
    @ResponseStatus(HttpStatus.OK)
    fun getAssigned(): List<TicketDTO> {
        return ticketService.getAssigned()
    }

}