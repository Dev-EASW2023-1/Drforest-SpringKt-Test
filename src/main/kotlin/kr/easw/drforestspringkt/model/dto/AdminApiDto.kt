package kr.easw.drforestspringkt.model.dto

import java.util.*

data class AddUserNoticeRequest(val contents: String, val executeDeviceNotification: Boolean = false)

data class AddUserNoticeResponse(val isSuccess: Boolean, val message: String)

data class AddAnnouncementRequest(val region: String?, val author: String, val title: String, val contents: String)

data class AddAnnouncementResponse(val message: String)

data class DeleteAnnouncementResponse(val isSuccess: Boolean)

data class AddRegionResponse(val isSuccess: Boolean)

data class DeleteRegionResponse(val isSuccess: Boolean)

data class ListAnnouncementResponse(val announcements: List<AnnouncementEntityData>)

data class AnnouncementEntityData(val id: Long, val time: Date, val title: String, val contents: String)