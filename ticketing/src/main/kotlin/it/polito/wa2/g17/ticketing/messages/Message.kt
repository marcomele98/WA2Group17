package it.polito.wa2.g17.ticketing.messages

import it.polito.wa2.g17.ticketing.EntityBase
import it.polito.wa2.g17.ticketing.attachments.Attachment
import it.polito.wa2.g17.ticketing.tickets.Ticket
import jakarta.persistence.*
import java.util.Date

@Entity
class Message(

    @Column(nullable = false)
    var text: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    var timestamp: Date = Date(),

    @ManyToOne
    val ticket: Ticket,

    val userId: Long,

    @OneToMany(mappedBy = "message", cascade = [CascadeType.ALL])
    var attachments: MutableList<Attachment> = mutableListOf(),

    ) : EntityBase<Long>() {
    constructor() : this("", Date(), Ticket(), 0)
    }

