package it.polito.wa2.g17.server.ticketing.attachments

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import jakarta.persistence.Id
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/*
@RestController
@Validated
@RequestMapping("/API/tickets")
class AttachmentController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/file/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun handleFileUpload ( @RequestParam("file") file: MultipartFile ): Long {
        return 1L;
    }*/
    
