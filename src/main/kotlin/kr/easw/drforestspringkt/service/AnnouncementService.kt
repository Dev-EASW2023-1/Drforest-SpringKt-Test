package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.enumeration.Roles
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.AnnouncementEntity
import kr.easw.drforestspringkt.model.repository.AnnouncementRepository
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class AnnouncementService(
    val repo: AnnouncementRepository,
    val regionService: RegionManagementService,
    val permissionService: RegionPermissionService,
    val authenticateService: AuthenticateService
) {
    fun getAnnouncement(id: Long): AnnouncementData {
        val data = try {
            repo.getById(id)
        } catch (e: Exception) {
            throw ResourceNotFoundException("No announcement found for id $id")
        }
        return AnnouncementData(data.id, data.time!!, data.author ?: "관리자", data.region, data.title, data.content)
    }

    fun getAllAnnouncement(region: String? = null, includeGlobal: Boolean = true): AnnouncementResponse {
        return AnnouncementResponse(
            LinkedHashMap<Long, AnnouncementData>().run {
                repo.getAllByRegionOrderByIdDesc(region).forEach { data ->
                    put(
                        data.id,
                        AnnouncementData(
                            data.id,
                            data.time!!,
                            data.author ?: "관리자",
                            data.region,
                            data.title,
                            data.content
                        )
                    )
                }
                if (includeGlobal && region != null) {
                    repo.getAllByRegionOrderByIdDesc(null).forEach { data ->
                        put(
                            data.id,
                            AnnouncementData(
                                data.id,
                                data.time!!,
                                data.author ?: "관리자",
                                data.region,
                                data.title,
                                data.content
                            )
                        )
                    }
                    return@run sortedMapOf(reverseOrder(), *entries.map { x -> x.key to x.value }.toTypedArray())
                }
                return@run this
            }
        )
    }

    fun addAnnouncement(author: String, region: String? = null, title: String, contents: String) {
        repo.save(AnnouncementEntity(author, region, title, contents)).id
    }

    fun addAnnouncement(request: AddAnnouncementRequest): AddAnnouncementResponse {
        if (regionService.getRegion(request.region!!) == null)
            return AddAnnouncementResponse(false, "등록되지 않은 지역입니다.")
        addAnnouncement(request.author, request.region, request.title, request.contents)
        return AddAnnouncementResponse(true, "새 공지가 추가되었습니다.")
    }

    fun addAnnouncement(request: AddGlobalAnnouncementRequest): AddAnnouncementResponse {
        addAnnouncement(request.author, null, request.title, request.contents)
        return AddAnnouncementResponse(true, "새 공지가 추가되었습니다.")
    }

    fun deleteAnnouncement(announcementId: Long): Boolean {
        try {
            repo.getReferenceById(announcementId)
        } catch (e: Throwable) {
            return false
        }
        repo.delete(AnnouncementEntity("", "", "", "").apply {
            id = announcementId
        })
        return true
    }

    fun addAnnouncement(user: UserAccountData, request: AddAnnouncementRequest): AddAnnouncementResponse {
        if (request.region == null && !user.authorities.contains(Roles.ADMIN))
            throw BadCredentialsException("해당 사용자는 전역 공지사항에 접근할 수 있는 권한이 존재하지 않습니다.")
        val region =
            if (request.region == null)
                null
            else
                regionService.getRegion(request.region) ?: throw ResourceNotFoundException("등록되지 않은 지역입니다.")
        val userData = authenticateService.findUserDataByName(user.username)
        if (region != null && !permissionService.isRegionManager(userData!!, region)) {
            throw BadCredentialsException("해당 유저는 대상 지역 공지사항에 접근할 수 있는 권한이 존재하지 않습니다.")
        }
        return addAnnouncement(request)
    }

    fun deleteAnnouncement(user: UserAccountData, announcementId: Long): Boolean {
        val userData = authenticateService.findUserDataByName(user.username)
        val region = getAnnouncement(announcementId).region
        if (region != null) {
            val regionInstance = regionService.getRegion(region) ?: throw BadCredentialsException("잘못된 지역명입니다.")
            if (!permissionService.isRegionManager(userData!!, regionInstance)) {
                throw BadCredentialsException("해당 유저는 대상 지역 공지사항에 접근할 수 있는 권한이 존재하지 않습니다.")
            }
        } else {
            if (!user.authorities.contains(Roles.ADMIN))
                throw BadCredentialsException("해당 유저는 대상 지역 공지사항에 접근할 수 있는 권한이 존재하지 않습니다.")
        }
        return deleteAnnouncement(announcementId)
    }

    fun getAnnouncementOfUserRegion(user: UserAccountData): AnnouncementResponse {
        val regions = permissionService.getAccessibleRegions(authenticateService.findAccountByUserId(user.username)!!)
        val announcementList = mutableListOf<AnnouncementData>()
        regions.forEach {
            announcementList += repo.getAllByRegionOrderByIdDesc(it.regionName).map { data ->
                AnnouncementData(
                    data.id,
                    data.time!!,
                    data.author ?: "관리자",
                    data.region,
                    data.title,
                    data.content
                )
            }
        }
        regions.sortedByDescending { x -> x.id }
        return AnnouncementResponse(announcementList.associateBy { it.id })
    }

    fun getUserAnnouncement(user: UserAccountData): AnnouncementResponse {
        val userData = authenticateService.getUserData(user)
        return getAllAnnouncement(region = userData.region, true)
    }
}

