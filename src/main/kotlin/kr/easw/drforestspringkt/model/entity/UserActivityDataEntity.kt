package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "UserActivity")
class UserActivityDataEntity(
    entity: UserAccountEntity,
    fieldName: String,
    fieldValue: Float
) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    var entity: UserAccountEntity = entity

    @Column
    var fieldName: String = fieldName

    @Column
    var fieldValue = fieldValue

    @CreationTimestamp
    var timestamp: Date? = null
}
