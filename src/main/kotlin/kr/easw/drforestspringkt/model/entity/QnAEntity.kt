package kr.easw.drforestspringkt.model.entity

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*

@Entity
@javax.persistence.Table(name = "QnA")
class QnAEntity(
    user: UserAccountEntity,
    title: String,
    content: String
) {
    @Id
    @GeneratedValue
    var id: Long? = null

    @ManyToOne
    var user: UserAccountEntity = user

    @Column
    var title: String = title

    @Column
    var content: String = content

    @CreationTimestamp
    var createdTime: Date? = null

    @UpdateTimestamp
    var updatedTime: Date? = null

    @Column(nullable = true)
    var answerTitle: String? = null

    @Column(nullable = true)
    var answerContent: String? = null
}
