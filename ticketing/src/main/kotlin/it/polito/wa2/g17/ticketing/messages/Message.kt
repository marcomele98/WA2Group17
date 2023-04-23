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

    @Column(nullable = false)
    val userId: Long,

    @ManyToOne
    var ticket: Ticket? = null,

    @OneToMany(mappedBy = "message", cascade = [CascadeType.ALL])
    var attachments: MutableList<Attachment> = mutableListOf(),

    ) : EntityBase<Long>() {

    fun addAttachments(a: List<Attachment>) {
        a.forEach { it.message = this }
        attachments.addAll(a)
    }

}

