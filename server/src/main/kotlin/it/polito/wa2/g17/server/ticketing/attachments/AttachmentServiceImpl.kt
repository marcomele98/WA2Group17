package it.polito.wa2.g17.server.ticketing.attachments

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
@Transactional
class AttachmentServiceImpl(private val attachmentRepository: AttachmentRepository): AttachmentService {
    override fun uploadAttachment(file: MultipartFile) {
        val fileName = file.originalFilename!!
        val fileType = file.contentType!!
        val fileContent = file.bytes

        val newAttachment = Attachment(fileName, fileType, fileContent)
        attachmentRepository.save(newAttachment)
    }
}