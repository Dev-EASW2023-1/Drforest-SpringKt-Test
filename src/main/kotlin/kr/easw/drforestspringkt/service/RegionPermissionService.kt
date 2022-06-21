package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.enumeration.Roles
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.RegionEntity
import kr.easw.drforestspringkt.model.entity.RegionPermissionEntity
import kr.easw.drforestspringkt.model.entity.UserDataEntity
import kr.easw.drforestspringkt.model.repository.RegionPermissionRepository
import org.springframework.stereotype.Service

@Service
class RegionPermissionService(
    val authenticateService: AuthenticateService,
    val regionService: RegionManagementService,
    val permissionRepository: RegionPermissionRepository
) {
    fun isManager(userName: String): Boolean {
        return isManager(authenticateService.findAccountByUserId(userName) ?: return false)
    }

    fun isManager(user: UserDataEntity): Boolean {
        return Roles.listRoles(user.account.permission).contains(Roles.MANAGER)
    }

    fun isRegionManager(user: UserDataEntity, region: RegionEntity): Boolean {
        return permissionRepository.getAllByUser(user).map { x -> x.region }.contains(region)
    }

    fun addRegionPermission(user: UserDataEntity, region: RegionEntity): Boolean {
        if (isRegionManager(user, region))
            return false
        permissionRepository.save(RegionPermissionEntity(user, region))
        return true
    }

    fun setManagerPermission(user: UserDataEntity, isManager: Boolean) {
        if (isManager)
            authenticateService.addPermission(user.account.userId, Roles.MANAGER)
        else {
            authenticateService.removePermission(user.account.userId, Roles.MANAGER)
            permissionRepository.deleteAll(permissionRepository.getAllByUser(user))
        }
    }

    fun setManager(request: AddManagerRequest): AddManagerResponse {
        val user =
            authenticateService.findUserDataByName(request.userId) ?: return AddManagerResponse(
                false,
                "대상 유저가 존재하지 않습니다."
            )
        setManagerPermission(user, true)
        return AddManagerResponse(true, "관리자 추가에 성공하였습니다.")
    }


    fun deleteManager(request: DeleteManagerRequest): DeleteManagerResponse {
        val user =
            authenticateService.findUserDataByName(request.userId) ?: return DeleteManagerResponse(
                false,
                "대상 유저가 존재하지 않습니다."
            )
        setManagerPermission(user, false)
        return DeleteManagerResponse(true, "관리자 삭제에 성공하였습니다.")
    }

    fun addRegionPermission(request: AddRegionToManagerRequest): AddRegionToManagerResponse {
        val user =
            authenticateService.findUserDataByName(request.userId) ?: return AddRegionToManagerResponse(
                false,
                "대상 유저가 존재하지 않습니다."
            )
        val region = regionService.getRegion(request.region) ?: return AddRegionToManagerResponse(
            false,
            "대상 지역이 존재하지 않습니다."
        )
        if (isRegionManager(user, region))
            return AddRegionToManagerResponse(false, "이미 추가된 관리 지역입니다.")
        permissionRepository.save(RegionPermissionEntity(user, region))
        return AddRegionToManagerResponse(true, "관리 지역이 추가되었습니다.")
    }

    fun getAccessibleRegions(userData: UserDataEntity): List<RegionEntity> {
        return permissionRepository.getAllByUser(userData).map { x -> x.region }
    }
}