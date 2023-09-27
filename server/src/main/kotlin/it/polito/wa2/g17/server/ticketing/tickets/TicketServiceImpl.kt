package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.profiles.ProfileNotFoundException
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.profiles.toDTO
import it.polito.wa2.g17.server.ticketing.attachments.Attachment
import it.polito.wa2.g17.server.ticketing.attachments.AttachmentRepository
import it.polito.wa2.g17.server.ticketing.messages.*
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import it.polito.wa2.g17.server.ticketing.status.toDTO
import it.polito.wa2.g17.server.ticketing.warranties.WarrantyNotFoundException
import it.polito.wa2.g17.server.ticketing.warranties.WarrantyRepository
import it.polito.wa2.g17.server.ticketing.warranties.toGetWarrantyWithCustomerDTO
import it.polito.wa2.g17.server.ticketing.warranties.withCustomer
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val attachmentRepository: AttachmentRepository,
    private val profileRepository: ProfileRepository,
    private val warrantyRepository: WarrantyRepository
) : TicketService {


    //TODO: controllo sul ruolo

    override fun createTicket(ticketDTO: CreateTicketDTO, email: String): TicketWithMessagesDTO {

        val warranty = warrantyRepository.findById(ticketDTO.warrantyId)
            .orElseThrow { WarrantyNotFoundException("Warranty with ID ${ticketDTO.warrantyId} not found") }

        val date = Date()
        val ticket = Ticket(ticketDTO.problemType, ticketDTO.title, warranty)
        warranty.tickets.add(ticket)

        val message = ticketDTO.initialMessage
            .withTimestamp(date)
            .toEntity()
            .withUser(email)

        val attachments = attachmentRepository.findAllByIdIn(ticketDTO.initialMessage.attachmentIds)

        checkAttachments(attachments, warranty.customerEmail)

        message.addAttachments(attachments)

        val status = StatusChange(Status.OPEN, email, date)

        ticket.addMessage(message)
        ticket.addStatus(status)
        ticket.problemType = ticketDTO.problemType

        return ticketRepository.save(ticket).toWithMessagesDTO()
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
        val customer = profileRepository.findByEmail(ticket.warranty.customerEmail)
            ?: throw ProfileNotFoundException("Profile with email ${ticket.warranty.customerEmail} not found")
        var t = ticket.toCompleteDTO()
            .withWarranty(ticket.warranty.toGetWarrantyWithCustomerDTO().withCustomer(customer.toDTO()))
        if (ticket.expertEmail != null) {
            val expert = profileRepository.findByEmail(ticket.expertEmail!!)
                ?: throw ProfileNotFoundException("Customer with email ${ticket.expertEmail} not found")
            t = t.withExpert(expert.toDTO())
        }
        return t
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
        return warrantyRepository.findAllByCustomerEmail(customerEmail).flatMap {
            it.tickets
        }.map { it.toPartialDTO() }
    }

    override fun assignTicket(ticketId: Long, expertEmail: String, priority: Priority): TicketWithMessagesDTO {

        val expert = profileRepository.findByEmail(expertEmail)
            ?: throw ProfileNotFoundException("Expert with email $expertEmail not found")

        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if (!expert.skills.contains(ticket.problemType))
            throw WrongSkillsException("Expert with email $expertEmail is not skilled for problem type ${ticket.problemType}")

        if (ticket.status != Status.OPEN)
            throw WrongStateException("Ticket with ID $ticketId is not open")

        ticket.expertEmail = expertEmail
        ticket.priorityLevel = priority
        ticket.addStatus(StatusChange(Status.IN_PROGRESS, expertEmail))

        return ticketRepository.save(ticket).toWithMessagesDTO()
    }


    override fun closeTicket(ticketId: Long, userEmail: String, role: String): TicketWithMessagesDTO {
        profileRepository.findByEmail(userEmail)
            ?: throw ProfileNotFoundException("User with email $userEmail not found")
        //TODO: controllo sul profile non 404

        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if ((ticket.warranty.customerEmail != userEmail && ticket.expertEmail != userEmail) || role == "ROLE_MANAGER")
            throw WrongUserException("You are not allowed to close this ticket")

        if (ticket.status != Status.IN_PROGRESS && ticket.status != Status.OPEN)
            throw WrongStateException("Ticket with ID $ticketId is not in progress")

        ticket.addStatus(StatusChange(Status.CLOSED, userEmail))

        return ticketRepository.save(ticket).toWithMessagesDTO()
    }


    override fun reopenTicket(ticketId: Long, email: String): TicketWithMessagesDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if (ticket.warranty.customerEmail != email)
            throw WrongUserException("You are not allowed to reopen this ticket")

        if (ticket.status != Status.CLOSED && ticket.status != Status.RESOLVED)
            throw WrongStateException("Ticket with ID $ticketId is not closed")

        if(ticket.expertEmail == null)
            ticket.addStatus(StatusChange(Status.OPEN, ticket.warranty.customerEmail))
        else
            ticket.addStatus(StatusChange(Status.IN_PROGRESS, ticket.warranty.customerEmail))

        return ticketRepository.save(ticket).toWithMessagesDTO()
    }

    override fun resolveTicket(ticketId: Long, email: String): TicketWithMessagesDTO {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        if (ticket.expertEmail != email)
            throw WrongUserException("You are not allowed to resolve this ticket")

        if (ticket.status != Status.IN_PROGRESS)
            throw WrongStateException("Ticket with ID $ticketId is not in progress")

        ticket.addStatus(StatusChange(Status.RESOLVED, ticket.expertEmail!!))
        //TODO: arrivato qua!!! get dell'esperto. La warranty così com'è non ha i dettagli dell'utente

        return ticketRepository.save(ticket).toWithMessagesDTO()
    }

    override fun addMessage(
        ticketId: Long,
        messageDTO: MessageDTO,
        email: String,
        role: String
    ): TicketWithMessagesDTO {

        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw TicketNotFoundException("Ticket with ID $ticketId not found")

        profileRepository.findByEmail(email)
            ?: throw ProfileNotFoundException("User with email ${email} not found")

        if ((ticket.warranty.customerEmail != email && ticket.expertEmail != email) || role == "ROLE_MANAGER")
            throw WrongUserException("You are not allowed to add a message to this ticket")

        val message = messageDTO
            .withTimestamp(Date())
            .toEntity()
            .withUser(email)


        val attachments = attachmentRepository.findAllByIdIn(messageDTO.attachmentIds)

        checkAttachments(attachments, email)

        message.addAttachments(attachments)

        ticket.addMessage(message)

        return ticketRepository.save(ticket).toWithMessagesDTO()
    }

    private fun checkAttachments(
        attachments: List<Attachment>,
        email: String
    ) {
        if (attachments.any { attachment -> attachment.userEmail != email })
            throw WrongAttachmentsException("You are not allowed to add the selected attachments to this message")
    }


}