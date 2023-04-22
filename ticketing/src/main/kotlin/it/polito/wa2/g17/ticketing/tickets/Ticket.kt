package it.polito.wa2.g17.ticketing.tickets

import it.polito.wa2.g17.ticketing.EntityBase
import it.polito.wa2.g17.ticketing.messages.Message
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

    @OneToMany(mappedBy = "ticket")
    val messages: MutableList<Message>,

    @OneToMany(mappedBy = "ticket")
    val status: MutableList<StatusChange>,
) : EntityBase<Long>()