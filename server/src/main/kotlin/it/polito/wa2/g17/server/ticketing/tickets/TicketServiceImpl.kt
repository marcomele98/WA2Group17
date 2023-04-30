package it.polito.wa2.g17.server.ticketing.tickets;

import it.polito.wa2.g17.server.ticketing.attachments.AttachmentRepository
import it.polito.wa2.g17.server.ticketing.messages.*
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import it.polito.wa2.g17.server.ticketing.status.toDTO
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service;
import java.util.*

@Service
@Transactional
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val attachmentRepository: AttachmentRepository
) : TicketService {


    //TODO: controllo sul ruolo

    override fun createTicket(createTicketDTO: CreateTicketDTO): CompleteTicketDTO {

        val date = Date()
        val ticket = Ticket(createTicketDTO.customerEmail, createTicketDTO.productEan, createTicketDTO.problemType)

        val message = createTicketDTO.initialMessage
            .withTimestamp(date)
            .withUserEmail(ticket.customerEmail)
            .toEntity()

        message.addAttachments(attachmentRepository.findAllByIdIn(createTicketDTO.initialMessage.attachmentIds))

        val status = StatusChange(Status.OPEN, ticket.customerEmail, date)

        ticket.addMessage(message)
        ticket.addStatus(status)
        ticket.problemType = createTicketDTO.problemType

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

    override fun getUnresolvedByExpertEmail(expertEmail: String): List<PartialTicketDTO> {
        return ticketRepository.findAllByExpertEmailAndStatusIn(
            expertEmail, listOf(
                Status.IN_PROGRESS
            )
        ).map { it.toPartialDTO() }
    }

    override fun getResolvedByExpertEmail(expertEmail: String): List<PartialTicketDTO> {
        return ticketRepository.findAllByExpertEmailAndStatusIn(
            expertEmail, listOf(
                Status.CLOSED, Status.RESOLVED,
            )
        ).map { it.toPartialDTO() }
    }


    override fun getAllByCustomerEmail(customerEmail: String): List<PartialTicketDTO> {
        return ticketRepository.findAllByCustomerEmail(customerEmail).map { it.toPartialDTO() }
    }

    override fun assignTicket(ticketId: Long, expertEmail: String, priority: Priority): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if(ticket.status != Status.OPEN)
            throw WrongStateException("Ticket with ID $ticketId is not open")

        ticket.expertEmail = expertEmail
        ticket.priorityLevel = priority
        ticket.addStatus(StatusChange(Status.IN_PROGRESS, expertEmail))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun closeTicket(ticketId: Long, userEmail: String): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if(ticket.status != Status.IN_PROGRESS)
            throw WrongStateException("Ticket with ID $ticketId is not in progress")

        ticket.addStatus(StatusChange(Status.CLOSED, userEmail))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun reopenTicket(ticketId: Long): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if(ticket.status != Status.CLOSED && ticket.status != Status.RESOLVED)
            throw WrongStateException("Ticket with ID $ticketId is not closed")

        ticket.addStatus(StatusChange(Status.IN_PROGRESS, ticket.customerEmail!!))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun resolveTicket(ticketId: Long): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")
        if(ticket.status != Status.IN_PROGRESS)
            throw WrongStateException("Ticket with ID $ticketId is not in progress")

        ticket.addStatus(StatusChange(Status.RESOLVED, ticket.expertEmail!!))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun addMessage(ticketId: Long, messageDTO: MessageDTO): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        val message = messageDTO
            .withTimestamp(Date())
            .withUserEmail(messageDTO.userEmail!!)
            .toEntity()

        message.addAttachments(attachmentRepository.findAllByIdIn(messageDTO.attachmentIds))

        ticket.addMessage(message)

        return ticketRepository.save(ticket).toCompleteDTO()
    }


}