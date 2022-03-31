package kr.easw.drforestspringkt.model.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class RegionEntity(
    @Id var id: Long = 0L,
    @Column(unique = true, length = 60)
    var regionName: String,
)