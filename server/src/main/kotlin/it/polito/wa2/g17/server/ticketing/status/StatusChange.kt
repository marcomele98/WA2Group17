package it.polito.wa2.g17.server.ticketing.status

import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.tickets.Ticket
import jakarta.persistence.*
import java.util.Date


@Entity
@Table(name = "status_changes")
class StatusChange(

    @Enumerated(EnumType.STRING)
    var status: Status,

    var userEmail: String,

    @Temporal(TemporalType.TIMESTAMP)
    var timestamp: Date = Date(),

    @ManyToOne
    var ticket: Ticket? = null,
) : EntityBase<Long>() {
}