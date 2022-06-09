package kr.easw.drforestspringkt.model.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "User")
class UserDataEntity(user: UserAccountEntity, name: String, phone: String, region: RegionEntity) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @OneToOne
    var account: UserAccountEntity = user

    @Column(unique = false, length = 30)
    var name: String = name

    @Column(length = 30)
    var phone: String = phone

    @Column(nullable = true)
    var fcmToken: String? = null

    @ManyToOne
    var region: RegionEntity = region

    @CreationTimestamp
    var createdTimestamp: Date? = null


}
