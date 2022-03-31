package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class UserActivityDataEntity(
    @field:Id
    val id: Long = 0L,

    @field:Column
    @field:ManyToOne
    val entity: UserAccountEntity,

    @field:Column
    val fieldName: String,

    @field:Column
    val fieldValue: Float,

    @CreationTimestamp
    val timestamp: Date? = null,
)