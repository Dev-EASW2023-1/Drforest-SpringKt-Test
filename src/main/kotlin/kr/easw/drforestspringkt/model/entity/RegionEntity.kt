package kr.easw.drforestspringkt.model.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@javax.persistence.Table(name = "Region")
class RegionEntity(regionName: String) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @Column(unique = true, length = 60)
    var regionName: String = regionName
}
