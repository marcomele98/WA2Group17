package it.polito.wa2.g17.ticketing.messages

import it.polito.wa2.g17.ticketing.EntityBase
import it.polito.wa2.g17.ticketing.attachments.Attachment
import it.polito.wa2.g17.ticketing.tickets.Ticket
import jakarta.persistence.*
import java.util.Date

@Entity
class Message(

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var text: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    var timestamp: Date,

    @OneToMany(mappedBy = "message")
    val attachments: MutableList<Attachment> = mutableListOf(),

    @ManyToOne
    val ticket: Ticket,

    ): EntityBase<Long>()

