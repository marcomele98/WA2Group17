package it.polito.wa2.g17.ticketing.tickets;

import it.polito.wa2.g17.ticketing.attachments.toEntity
import it.polito.wa2.g17.ticketing.messages.*
import it.polito.wa2.g17.ticketing.status.Status
import it.polito.wa2.g17.ticketing.status.StatusChange
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TicketServiceImpl(private val ticketRepository: TicketRepository) : TicketService {


    //TODO: controllo sul ruolo

    override fun createTicket(createTicketDTO: CreateTicketDTO): TicketDTO {

        val date = Date()
        val ticket = Ticket(createTicketDTO.customerId, createTicketDTO.productEan)

        val message = createTicketDTO.initialMessage
            .withTimestamp(date)
            .withUserId(ticket.customerId)
            .toEntity()

        message.addAttachments(
            createTicketDTO
                .initialMessage.attachments
                .map { it.toEntity() }
        )

        val status = StatusChange(Status.OPEN, date, ticket.customerId)

        ticket.addMessage(message)
        ticket.addStatus(status)

        return ticketRepository.save(ticket).toDtoComplete()
    }


    override fun getOpen(): List<TicketDTO> {
        return ticketRepository.findAllByStatusIn(
            listOf(
                Status.OPEN
            )
        ).map { it.toDTO() }
    }


    override fun getAssigned(): List<TicketDTO> {
        return ticketRepository.findAllByStatusIn(
            listOf(
                Status.IN_PROGRESS, Status.CLOSED, Status.RESOLVED, Status.REOPEN
            )
        ).map { it.toDTO() }
    }


    override fun getTicket(id: Long): TicketDTO {
        val ticket = ticketRepository.findByIdOrNull(id)
            ?: throw TicketNotFoundException("Ticket with ID $id not found")
        return ticket.toDtoComplete()
    }


}