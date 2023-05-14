package it.polito.wa2.g17.server.ticketing.attachments

import org.springframework.web.multipart.MultipartFile

interface AttachmentService {
    fun uploadAttachment(file: MultipartFile, email: String, role: String) : Long
    fun downloadAttachment(id: Long, email: String): Attachment
}