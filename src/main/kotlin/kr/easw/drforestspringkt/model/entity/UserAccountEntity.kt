package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@javax.persistence.Table(name = "UserAccount")
class UserAccountEntity(id: String, password: String) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @Column(unique = true, nullable = false, length = 30)
    var userId: String = id

    @Column(nullable = false)
    var password: String = password

    @CreationTimestamp
    var timestamp: Date? = null
}
