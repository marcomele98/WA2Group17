package it.polito.wa2.g17.server.ticketing.attachments

import it.polito.wa2.g17.server.profiles.ProfileNotFoundException
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.ticketing.tickets.AttachmentNotFoundException
import it.polito.wa2.g17.server.ticketing.tickets.WrongUserException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class AttachmentServiceImpl(private val attachmentRepository: AttachmentRepository, private val profileRepository: ProfileRepository): AttachmentService {

    override fun uploadAttachment(file: MultipartFile, email : String, role: String): Long {
        val fileName = file.originalFilename!!
        val fileType = file.contentType!!
        val fileContent = file.bytes

        val profile = profileRepository.findByIdOrNull(email)
            ?: throw ProfileNotFoundException("User with email $email not found")

        if(role == "ROLE_MANAGER") {
            throw WrongUserException("You are not allowed to upload attachments")
        }

        val newAttachment = Attachment(fileName, profile, fileType, fileContent)
        return attachmentRepository.save(newAttachment).id!!
    }


     override fun downloadAttachment(id: Long, email: String): Attachment {

        profileRepository.findByIdOrNull(email)
            ?: throw ProfileNotFoundException("User with email $email not found")

         val attachment = attachmentRepository.findByIdOrNull(id)
             ?: throw AttachmentNotFoundException("Attachment with id $id not found")

         if( attachment.user!!.email != email) {
             throw WrongUserException("You are not allowed to download this attachment")
         }

        return Attachment(attachment!!.name, attachment!!.user, attachment.type, attachment.content)
    }
}