package it.polito.wa2.g17.server.ticketing.attachments

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttachmentRepository : JpaRepository<Attachment, Long> {

    fun findAllByIdIn(ids: List<Long>): List<Attachment>
}
