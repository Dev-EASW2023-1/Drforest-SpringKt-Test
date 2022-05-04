package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
@javax.persistence.Table(name = "Notice")
class UserNoticeEntity(user: UserDataEntity, content: String, isRead: Boolean) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @CreationTimestamp
    var time: Date? = null

    @ManyToOne
    var user: UserDataEntity = user

    @Column
    var content: String = content

    @Column
    var isRead: Boolean = isRead
}