package it.polito.wa2.g17.server.ticketing.attachments

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttachmentRepository : JpaRepository<Attachment, Long> {
}
