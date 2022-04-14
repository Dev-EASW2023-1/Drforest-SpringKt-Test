package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class AnnouncementEntity(
    @Id
    @GeneratedValue
    var id: Long = 0L,

    @CreationTimestamp
    var time: Date? = null,

    @Column
    var title: String,

    @Column
    var content: String
)