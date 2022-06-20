package kr.easw.drforestspringkt.model.repository

import kr.easw.drforestspringkt.model.entity.RegionPermissionEntity
import kr.easw.drforestspringkt.model.entity.UserAccountEntity
import kr.easw.drforestspringkt.model.entity.UserDataEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RegionPermissionRepository : JpaRepository<RegionPermissionEntity, Long> {
    fun getAllByUser(entity: UserDataEntity) : List<RegionPermissionEntity>
}