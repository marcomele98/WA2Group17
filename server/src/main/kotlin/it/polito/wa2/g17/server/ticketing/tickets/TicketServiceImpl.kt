package it.polito.wa2.g17.server.ticketing.tickets;

import it.polito.wa2.g17.server.ticketing.attachments.toEntity
import it.polito.wa2.g17.server.ticketing.messages.toEntity
import it.polito.wa2.g17.server.ticketing.messages.withTimestamp
import it.polito.wa2.g17.server.ticketing.messages.withUserId
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import it.polito.wa2.g17.server.ticketing.status.toDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TicketServiceImpl(private val ticketRepository: TicketRepository) : TicketService {


    //TODO: controllo sul ruolo

    override fun createTicket(createTicketDTO: CreateTicketDTO): CompleteTicketDTO {

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

        return ticketRepository.save(ticket).toCompleteDTO()
    }


    override fun getAllOpen(): List<PartialTicketDTO> {
        return ticketRepository.findAllByStatusIn(
            listOf(
                Status.OPEN
            )
        ).map { it.toPartialDTO() }
    }


    override fun getAllAssigned(): List<PartialTicketDTO> {
        return ticketRepository.findAllByStatusIn(
            listOf(
                Status.IN_PROGRESS, Status.CLOSED, Status.RESOLVED, Status.REOPEN
            )
        ).map { it.toPartialDTO() }
    }


    override fun getTicket(id: Long): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(id)
            ?: throw TicketNotFoundException("Ticket with ID $id not found")
        return ticket.toCompleteDTO()
    }

    override fun getStatusHistory(id: Long): List<StatusChangeDTO> {
        val ticket = ticketRepository.findByIdOrNull(id)
            ?: throw TicketNotFoundException("Ticket with ID $id not found")
        return ticket.statusHistory.map { it.toDTO() }
    }

    override fun getUnresolvedByExpertId(expertId: Long): List<PartialTicketDTO> {
        return ticketRepository.findAllByExpertIdAndStatusIn(
            expertId, listOf(
                Status.IN_PROGRESS, Status.REOPEN
            )
        ).map { it.toPartialDTO() }
    }

    override fun getResolvedByExpertId(expertId: Long): List<PartialTicketDTO> {
        return ticketRepository.findAllByExpertIdAndStatusIn(
            expertId, listOf(
                Status.CLOSED, Status.RESOLVED,
            )
        ).map { it.toPartialDTO() }
    }


    override fun getAllByCustomerId(customerId: Long): List<PartialTicketDTO> {
        return ticketRepository.findAllByCustomerId(customerId).map { it.toPartialDTO() }
    }


}