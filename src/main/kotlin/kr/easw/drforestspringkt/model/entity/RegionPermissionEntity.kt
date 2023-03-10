package kr.easw.drforestspringkt.model.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
@javax.persistence.Table(name = "RegionPermission")
class RegionPermissionEntity (user: UserDataEntity, region: RegionEntity) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    var user: UserDataEntity = user

    @ManyToOne()
    var region: RegionEntity = region


}