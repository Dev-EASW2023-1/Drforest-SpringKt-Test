package kr.easw.drforestspringkt.model.entity

import javax.persistence.*

@Entity
@javax.persistence.Table(name = "SharedUser")
class SharedUserEntity(user: UserDataEntity, target: UserDataEntity, isShared: Boolean) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    var user: UserDataEntity = user


    @ManyToOne
    var target: UserDataEntity = target

    @Column
    var isShared: Boolean = isShared

}
