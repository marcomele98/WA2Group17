package it.polito.wa2.g17.ticketing.status

import it.polito.wa2.g17.ticketing.EntityBase
import it.polito.wa2.g17.ticketing.tickets.Ticket
import jakarta.persistence.*
import java.util.Date


@Entity
@Table(name = "status_changes")
class StatusChange(
    var status: Status,

    @Temporal(TemporalType.TIMESTAMP)
    var timestamp: Date,

    var userId: Long,

    @ManyToOne
    var ticket: Ticket
) : EntityBase<Long>() {
}