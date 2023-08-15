package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.products.Product
import it.polito.wa2.g17.server.profiles.Profile
import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.messages.Message
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.warranties.Warranty
import jakarta.persistence.*

@Entity
@Table(name = "tickets")
class Ticket(

    @Enumerated(EnumType.STRING)
    var problemType: ProblemType,

    @ManyToOne
    @JoinColumn(name = "warranty_id", referencedColumnName = "id")
    val warranty: Warranty,

    @Enumerated(EnumType.STRING)
    var priorityLevel: Priority? = null,

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    val messages: MutableList<Message> = mutableListOf(),

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    val statusHistory: MutableList<StatusChange> = mutableListOf(),

    @Column(nullable = true, name = "expert_email")
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
