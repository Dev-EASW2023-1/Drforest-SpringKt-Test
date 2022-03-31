package kr.easw.drforestspringkt.model.entity

import javax.persistence.*

@Entity
class UserDataEntity(
    @Id
    var id: Long = 0L,

    @OneToOne
    @Column
    var account: UserAccountEntity,

    @Column
    @ManyToOne
    @JoinColumn(name = "region_id")
    var region: RegionEntity,
)