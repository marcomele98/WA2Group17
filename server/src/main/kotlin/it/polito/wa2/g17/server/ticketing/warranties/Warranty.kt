package it.polito.wa2.g17.server.ticketing.warranties

import it.polito.wa2.g17.server.products.Product
import it.polito.wa2.g17.server.profiles.Profile
import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.tickets.Ticket
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "warranties")
class Warranty(

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    var startDate: Date = Date(),

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    var endDate: Date = Date(),

    @OneToMany(mappedBy = "warranty", cascade = [CascadeType.ALL])
    var tickets: MutableList<Ticket> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "product_ean", nullable = false, referencedColumnName = "ean")
    var product: Product,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val typology: Typology,

    @ManyToOne
    @JoinColumn(name = "customer_email", nullable = false, referencedColumnName = "email")
    var customer: Profile,

    ): EntityBase<Long>() {
}