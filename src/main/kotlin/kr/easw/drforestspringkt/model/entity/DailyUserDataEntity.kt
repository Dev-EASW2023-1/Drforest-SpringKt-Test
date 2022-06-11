package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity
@javax.persistence.Table(name = "DailyUserActivity")
class DailyUserDataEntity(
    entity: UserAccountEntity,
    fieldName: String,
    fieldValue: Int
) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    var entity: UserAccountEntity = entity

    @Column
    var fieldName: String = fieldName

    @Column
    var fieldValue : Int = fieldValue

    @CreationTimestamp
    var timestamp: Date? = null
}
