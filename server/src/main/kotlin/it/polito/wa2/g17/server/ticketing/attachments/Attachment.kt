package it.polito.wa2.g17.server.ticketing.attachments

import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.messages.Message
import jakarta.persistence.*

@Entity
//@Table(name = "attachments")
class Attachment(

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, name = "user_email")
    var userEmail: String? = null,

    @Column(nullable = false)
    var type: String,

    @Lob
    @Column(nullable = false)
    var content: ByteArray,

    @ManyToOne
    var message: Message? = null,

    ): EntityBase<Long>()