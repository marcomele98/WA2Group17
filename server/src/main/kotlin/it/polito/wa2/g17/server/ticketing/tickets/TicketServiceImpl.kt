package it.polito.wa2.g17.server.ticketing.tickets;

import it.polito.wa2.g17.server.ticketing.attachments.toEntity
import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
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

        val status = StatusChange(Status.OPEN, ticket.customerId, date)

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
                Status.IN_PROGRESS, Status.CLOSED, Status.RESOLVED
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
                Status.IN_PROGRESS
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

    override fun assignTicket(ticketId: Long, expertId: Long): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        ticket.expertId = expertId
        ticket.addStatus(StatusChange(Status.IN_PROGRESS, expertId))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun closeTicket(ticketId: Long, userId: Long): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        ticket.addStatus(StatusChange(Status.CLOSED, userId))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun reopenTicket(ticketId: Long): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        ticket.addStatus(StatusChange(Status.IN_PROGRESS, ticket.customerId!!))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun resolveTicket(ticketId: Long): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        ticket.addStatus(StatusChange(Status.RESOLVED, ticket.expertId!!))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun addMessage(ticketId: Long, messageDTO: MessageDTO): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        val message = messageDTO
            .withTimestamp(Date())
            .withUserId(messageDTO.userId!!)
            .toEntity()

        message.addAttachments(
            messageDTO.attachments
                .map { it.toEntity() }
        )

        ticket.addMessage(message)

        return ticketRepository.save(ticket).toCompleteDTO()
    }


}