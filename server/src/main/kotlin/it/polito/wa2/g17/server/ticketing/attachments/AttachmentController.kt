package it.polito.wa2.g17.server.ticketing.attachments

import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@Validated
@RequestMapping("/API/tickets")
class AttachmentController(private val attachment: AttachmentService) {
    @PostMapping("/upload/attachment", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(@RequestPart("file") file: MultipartFile) {
        attachment.uploadAttachment(file)
    }
}
    
