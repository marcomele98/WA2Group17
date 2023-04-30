package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.ProblemType
import it.polito.wa2.g17.server.ticketing.messages.Message
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import jakarta.persistence.*

@Entity
@Table(name = "tickets")
class Ticket(
    @Column(nullable = false)
    var customerEmail: String,

    @Column(nullable = false)
    var productEan: String,

    @Enumerated(EnumType.STRING)
    var problemType: ProblemType,

    var priorityLevel: Priority? = null,

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    val messages: MutableList<Message> = mutableListOf(),

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    val statusHistory: MutableList<StatusChange> = mutableListOf(),

    var expertEmail: String? = null,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.OPEN


) : EntityBase<Long>() {

    fun addMessage(m : Message) {
        m.ticket = this;
        messages.add(m)
    }

    fun addStatus(s: StatusChange) {
        s.ticket = this;
        status = s.status
        statusHistory.add(s)
    }

}