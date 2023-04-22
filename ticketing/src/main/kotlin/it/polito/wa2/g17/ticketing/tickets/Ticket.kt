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

    var expertId: Long,

    @Column(nullable = false)
    var productEan: String,

    var priorityLevel: Long,

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    val messages: MutableList<Message>,

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    val statusHistory: MutableList<StatusChange>,

    @Enumerated(EnumType.STRING)
    val status: Status = Status.OPEN

) : EntityBase<Long>() {
    constructor() : this(0, 0, "", 0, mutableListOf(), mutableListOf())
}