package it.polito.wa2.g17.ticketing.attachments

import it.polito.wa2.g17.ticketing.EntityBase
import it.polito.wa2.g17.ticketing.messages.Message
import it.polito.wa2.g17.ticketing.tickets.Ticket
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Attachment(

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var type: String,

    @Lob
    @Column(nullable = false)
    var content: ByteArray,

    @ManyToOne
    var message: Message,

    ): EntityBase<Long>()