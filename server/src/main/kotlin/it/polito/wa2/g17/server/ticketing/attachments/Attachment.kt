package it.polito.wa2.g17.server.ticketing.attachments

import it.polito.wa2.g17.server.profiles.Profile
import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.messages.Message
import jakarta.persistence.*

@Entity
//@Table(name = "attachments")
class Attachment(

    @Column(nullable = false)
    var name: String,

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    var user: Profile? = null,

    @Column(nullable = false)
    var type: String,

    @Lob
    @Column(nullable = false)
    var content: ByteArray,

    @ManyToOne
    var message: Message? = null,

    ): EntityBase<Long>()