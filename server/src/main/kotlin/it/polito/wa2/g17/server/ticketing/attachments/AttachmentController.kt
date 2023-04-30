package it.polito.wa2.g17.server.ticketing.attachments

import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.ByteArrayInputStream


@RestController
@Validated
@RequestMapping("/API/attachments")
class AttachmentController(private val attachmentService: AttachmentService) {
    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadFile(@RequestPart("file") file: MultipartFile): Long {
        return attachmentService.uploadAttachment(file)
    }

    @GetMapping("/download/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun downloadFile(@PathVariable id: Long, response: HttpServletResponse): ResponseEntity<InputStreamResource> {
        val attachment = attachmentService.downloadAttachment(id)
        val inputStream = ByteArrayInputStream(attachment.content)
        val headers = HttpHeaders()
        headers.add("Content-Disposition", "attachment; filename=${attachment.name}")
        headers.add("Content-Type", attachment.type)
        return ResponseEntity.ok().headers(headers).body(InputStreamResource(inputStream))
    }

}
    
