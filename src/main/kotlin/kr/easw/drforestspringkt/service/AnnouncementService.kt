package kr.easw.drforestspringkt.service

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
        return AnnouncementData(data.id, data.time!!, data.title, data.content)
    }

    fun getAllAnnouncement(): AnnouncementResponse {
        return AnnouncementResponse(
            LinkedHashMap<Long, AnnouncementData>().apply {
                repo.getAllByOrderByIdDesc().forEach { data ->
                    put(data.id, AnnouncementData(data.id, data.time!!, data.title, data.content))
                }
            }
        )
    }

    fun addAnnouncement(title: String, contents: String) {
        repo.save(AnnouncementEntity(title, contents)).id
    }
}

