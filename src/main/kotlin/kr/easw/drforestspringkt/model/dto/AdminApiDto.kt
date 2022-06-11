package kr.easw.drforestspringkt.model.dto

import java.util.*

data class AddUserNoticeRequest(val contents: String, val executeDeviceNotification: Boolean = false)

data class AddUserNoticeResponse(val isSuccess: Boolean, val message: String)

data class AddAnnouncementRequest(val title: String, val contents: String)

data class AddAnnouncementResponse(val message: String)

data class AddRegionRequest(val regionName: String)

data class AddRegionResponse(val isSuccess: Boolean, val message: String)

data class ListAnnouncementResponse(val announcements: List<AnnouncementEntityData>)

data class AnnouncementEntityData(val id: Long, val time: Date, val title: String, val contents: String)