package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class UserAccountEntity(
    @Id var id: Long = 0L,
    @field:Column(unique = true, nullable = false) var userId: String = "",
    @field:Column(nullable = false) var password: String = "",
    @field:CreationTimestamp var timeStamp: LocalDateTime? = null,
)