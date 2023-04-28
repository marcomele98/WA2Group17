package it.polito.wa2.g17.server.ticketing.attachments

import it.polito.wa2.g17.server.ticketing.EntityBase
import it.polito.wa2.g17.server.ticketing.messages.Message
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne

@Entity
class Attachment(

    @Column(nullable = false)
    var name: String,

    /*@Column(nullable = false)
    var type: String,

    @Lob
    @Column(nullable = false)
    var content: ByteArray,*/

    ) : EntityBase<Long>()