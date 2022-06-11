package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.dto.AddAnnouncementRequest
import kr.easw.drforestspringkt.model.dto.AddAnnouncementResponse
import kr.easw.drforestspringkt.model.dto.AnnouncementData
import kr.easw.drforestspringkt.model.dto.AnnouncementResponse
import kr.easw.drforestspringkt.model.entity.AnnouncementEntity
import kr.easw.drforestspringkt.model.repository.AnnouncementRepository
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class AnnouncementService(
    val repo: AnnouncementRepository
) {
    fun getAnnouncement(id: Long): AnnouncementData {
        val data = try {
            repo.getById(id)
        } catch (e: Exception) {
            throw ResourceNotFoundException("No announcement found for id $id")
        }
        return AnnouncementData(data.id, data.time!!, data.author, data.region, data.title, data.content)
    }

    fun getAllAnnouncement(region: String? = null, includeGlobal: Boolean = true): AnnouncementResponse {
        return AnnouncementResponse(
            LinkedHashMap<Long, AnnouncementData>().run {
                repo.getAllByRegionOrderByIdDesc(region).forEach { data ->
                    put(data.id, AnnouncementData(data.id, data.time!!, data.author, data.region, data.title, data.content))
                }
                if (includeGlobal && region != null) {
                    repo.getAllByRegionOrderByIdDesc(null).forEach { data ->
                        put(data.id, AnnouncementData(data.id, data.time!!, data.author, data.region, data.title, data.content))
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
        addAnnouncement(request.author, request.region, request.title, request.contents)
        return AddAnnouncementResponse("새 공지가 추가되었습니다.")
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
}

