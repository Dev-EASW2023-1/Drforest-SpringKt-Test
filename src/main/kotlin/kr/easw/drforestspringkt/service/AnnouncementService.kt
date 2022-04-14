package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.model.dto.AnnouncementDto
import kr.easw.drforestspringkt.model.dto.AnnouncementResponse
import kr.easw.drforestspringkt.model.repository.AnnouncementRepository
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class AnnouncementService(
    val repo: AnnouncementRepository
) {
    fun getAnnouncement(id: Long): AnnouncementDto {
        val data = repo.getById(id).orElseThrow { ResourceNotFoundException("No announcement found for id $id") }
        return AnnouncementDto(data.id, data.time!!, data.title, data.content)
    }

    fun getAllAnnouncement(): AnnouncementResponse {
        return AnnouncementResponse(
            LinkedHashMap<Long, AnnouncementDto>().apply {
                repo.getAllByOrderByIdDesc().forEach { data ->
                    put(data.id, AnnouncementDto(data.id, data.time!!, data.title, data.content))
                }
            }
        )
    }
}