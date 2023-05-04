package it.polito.wa2.g17.server.ticketing.messages

import it.polito.wa2.g17.server.profiles.Profile
import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.attachments.Attachment
import it.polito.wa2.g17.server.ticketing.tickets.Ticket
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
    @JoinColumn(name = "user_email", nullable = false, referencedColumnName = "email")
    var user: Profile? = null,

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

fun Message.withUser(p: Profile): Message {
    this.user = p
    return this
}

