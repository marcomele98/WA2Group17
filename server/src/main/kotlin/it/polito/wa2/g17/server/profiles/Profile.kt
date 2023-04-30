package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.ProblemType
import jakarta.persistence.*

@Entity
@Table(name = "profiles")
class Profile {
    @Id
    @Column(nullable = false)
    var email = ""
    @Column(nullable = false)
    var name = ""
    @Column(nullable = false)
    var surname = ""
}

