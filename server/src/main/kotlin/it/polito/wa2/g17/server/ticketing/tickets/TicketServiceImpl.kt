package it.polito.wa2.g17.server.ticketing.tickets;

import it.polito.wa2.g17.server.products.ProductNotFoundException
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.Profile
import it.polito.wa2.g17.server.profiles.ProfileNotFoundException
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.ticketing.attachments.AttachmentRepository
import it.polito.wa2.g17.server.ticketing.messages.*
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import it.polito.wa2.g17.server.ticketing.status.toDTO
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service;
import java.util.*

@Service
@Transactional
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val attachmentRepository: AttachmentRepository,
    private val profileRepository: ProfileRepository,
    private val productRepository: ProductRepository
) : TicketService {


    //TODO: controllo sul ruolo

    override fun createTicket(createTicketDTO: CreateTicketDTO, email: String): CompleteTicketDTO {
        val profile = profileRepository.findByIdOrNull(email)
            ?: throw ProfileNotFoundException("Customer with email $email not found")

        println(profile)

        val product = productRepository.findByIdOrNull(createTicketDTO.productEan)?: throw ProductNotFoundException("Product with EAN ${createTicketDTO.productEan} not found")

        val date = Date()
        val ticket = Ticket(profile, product, createTicketDTO.problemType)

        val message = createTicketDTO.initialMessage
            .withTimestamp(date)
            .toEntity()
            .withUser(profile)

        message.addAttachments(
            attachmentRepository.findAllByIdIn(createTicketDTO.initialMessage.attachmentIds)
        )

        val status = StatusChange(Status.OPEN, profile, date)

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

    override fun getUnresolvedByExpertEmail(email: String): List<PartialTicketDTO> {
        return ticketRepository.findAllByExpertEmailAndStatusIn(
            email, listOf(
                Status.IN_PROGRESS
            )
        ).map { it.toPartialDTO() }
    }

    override fun getResolvedByExpertEmail(email: String): List<PartialTicketDTO> {
        return ticketRepository.findAllByExpertEmailAndStatusIn(
            email, listOf(
                Status.CLOSED, Status.RESOLVED,
            )
        ).map { it.toPartialDTO() }
    }


    override fun getAllByCustomerEmail(customerEmail: String): List<PartialTicketDTO> {
        return ticketRepository.findAllByCustomerEmail(customerEmail).map { it.toPartialDTO() }
    }

    override fun assignTicket(ticketId: Long, expertEmail: String, priority: Priority): CompleteTicketDTO {

        val expert = profileRepository.findByIdOrNull(expertEmail)
            ?: throw ProfileNotFoundException("Expert with email $expertEmail not found")

        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if(!expert.skills.contains(ticket.problemType))
            throw WrongSkillsException("Expert with email $expertEmail is not skilled for problem type ${ticket.problemType}")

        if(ticket.status != Status.OPEN)
            throw WrongStateException("Ticket with ID $ticketId is not open")

        ticket.expert = expert
        ticket.priorityLevel = priority
        ticket.addStatus(StatusChange(Status.IN_PROGRESS, expert))

        return ticketRepository.save(ticket).toCompleteDTO()
    }


    override fun closeTicket(ticketId: Long, userEmail: String): CompleteTicketDTO {
        val user: Profile = profileRepository.findByIdOrNull(userEmail)
            ?: throw ProfileNotFoundException("User with email $userEmail not found")
        //TODO: controllo sul profile non 404

        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if(ticket.customer.email != userEmail && ticket.expert?.email != userEmail)
            throw WrongUserException("You are not allowed to close this ticket")

        if(ticket.status != Status.IN_PROGRESS)
            throw WrongStateException("Ticket with ID $ticketId is not in progress")

        ticket.addStatus(StatusChange(Status.CLOSED, user))

        return ticketRepository.save(ticket).toCompleteDTO()
    }



    override fun reopenTicket(ticketId: Long, email: String): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if(ticket.customer.email != email)
            throw WrongUserException("You are not allowed to reopen this ticket")

        if(ticket.status != Status.CLOSED && ticket.status != Status.RESOLVED)
            throw WrongStateException("Ticket with ID $ticketId is not closed")

        ticket.addStatus(StatusChange(Status.IN_PROGRESS, ticket.customer))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun resolveTicket(ticketId: Long, email: String): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if(ticket.expert?.email != email)
            throw WrongUserException("You are not allowed to resolve this ticket")

        if(ticket.status != Status.IN_PROGRESS)
            throw WrongStateException("Ticket with ID $ticketId is not in progress")

        ticket.addStatus(StatusChange(Status.RESOLVED, ticket.expert!!))

        return ticketRepository.save(ticket).toCompleteDTO()
    }

    override fun addMessage(ticketId: Long, messageDTO: MessageDTO, email: String): CompleteTicketDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        val profile = profileRepository.findByIdOrNull(email)
            ?: throw ProfileNotFoundException("User with email ${email} not found")

        if(ticket.customer.email != email && ticket.expert?.email != email)
            throw WrongUserException("You are not allowed to add a message to this ticket")

        val message = messageDTO
            .withTimestamp(Date())
            .toEntity()
            .withUser(profile)

        message.addAttachments(attachmentRepository.findAllByIdIn(messageDTO.attachmentIds))

        ticket.addMessage(message)

        return ticketRepository.save(ticket).toCompleteDTO()
    }


}