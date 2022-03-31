package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class QnAEntity(
    @field:Id
    val id: Long = 0,

    @field:Column
    @ManyToOne
    val user: UserAccountEntity,

    @field:Column
    val title: String,
    @field:Column
    val contents: String,
    @field:UpdateTimestamp
    val createdTime: Date? = null,

    @field:Column(nullable = true)
    val answerTitle: String?,

    @field:Column(nullable = true)
    val answerContents: String?,

    @field:UpdateTimestamp
    val updateTime: Date? = null,
)