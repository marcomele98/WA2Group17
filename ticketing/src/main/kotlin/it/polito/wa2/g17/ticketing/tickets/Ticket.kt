package it.polito.wa2.g17.ticketing.tickets

import it.polito.wa2.g17.ticketing.EntityBase
import it.polito.wa2.g17.ticketing.messages.Message
import it.polito.wa2.g17.ticketing.status.Status
import it.polito.wa2.g17.ticketing.status.StatusChange
import jakarta.persistence.*

@Entity
@Table(name = "tickets")
class Ticket(
    @Column(nullable = false)
    var customerId: Long,

    @Column(nullable = false)
    var productEan: String,

    var priorityLevel: Long? = null,

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    val messages: MutableList<Message> = mutableListOf(),

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    val statusHistory: MutableList<StatusChange> = mutableListOf(),

    var expertId: Long? = null,

    @Enumerated(EnumType.STRING)
    val status: Status = Status.OPEN

) : EntityBase<Long>() {

    fun addMessage(m : Message) {
        m.ticket = this;
        messages.add(m)
    }

    fun addStatus(s: StatusChange) {
        s.ticket = this;
        statusHistory.add(s)
    }

}