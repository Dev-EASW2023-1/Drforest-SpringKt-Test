package kr.easw.drforestspringkt.model.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
@javax.persistence.Table(name = "UserAccount")
class RegionPermissionEntity (user: UserDataEntity, region: RegionEntity) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    var user: UserDataEntity = user

    @ManyToOne(cascade = [CascadeType.REMOVE], optional = true)
    var region: RegionEntity = region


}