package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@javax.persistence.Table(name = "Announcement")
class AnnouncementEntity(author: String, region: String?, title: String, content: String) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @CreationTimestamp
    var time: Date? = null


    @Column(nullable = true)
    var author: String? = author

    @Column(nullable = true)
    var region: String? = region


    @Column
    var title: String = title

    @Column
    var content: String = content
}