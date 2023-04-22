package it.polito.wa2.g17.ticketing

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.util.ProxyUtils
import java.io.Serializable

@MappedSuperclass
abstract class EntityBase<T : Serializable> {

    companion object {
        private const val serialVersionUID = 1L
    }

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    var id: T? = null
        protected set

    override fun toString(): String {
        return "@Entity ${this.javaClass.name}(id=$id)"
    }


    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (javaClass != ProxyUtils.getUserClass(other))
            return false
        other as EntityBase<*>
        return if (null == id) false
        else this.id == other.id
    }


    override fun hashCode(): Int {
        return 31
    }

}