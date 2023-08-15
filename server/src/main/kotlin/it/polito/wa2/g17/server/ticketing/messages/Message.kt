package it.polito.wa2.g17.server.ticketing.messages

import it.polito.wa2.g17.server.profiles.Profile
import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.attachments.Attachment
import it.polito.wa2.g17.server.ticketing.tickets.Ticket
import jakarta.persistence.*
import java.util.*

@Entity
//@Table(name = "messages")
class Message(

    @Column(nullable = false)
    var text: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    var timestamp: Date = Date(),

    @Column(nullable = false, name = "user_email")
    var userEmail: String? = null,

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

fun Message.withUser(email: String): Message {
    this.userEmail = email
    return this
}

