package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.SharedUserEntity
import kr.easw.drforestspringkt.model.repository.SharedUserRepository
import org.springframework.stereotype.Service

@Service
class SharedUserService(val repo: SharedUserRepository, val userService: AuthenticateService) {

    fun findAllSharedUser(user: UserAccountData): SharedUserListResponse {
        return SharedUserListResponse(
            repo.findAllByUser_Account_UserId(user.username).map {
                SharedUserData(
                    it.target.account.userId,
                    it.target.name,
                    it.target.phone,
                    it.isShared
                )
            }
        )
    }

    fun acceptShare(user: UserAccountData, req: AcceptShareRequest): AcceptShareResponse {
        val userOrigin = userService.findAccountByUserId(req.userId)
            ?: return AcceptShareResponse(false, req.userId, "대상 유저는 공유를 신청하지 않았습니다.")

        val userTarget = userService.findAccountByUserId(user.username)
            ?: return AcceptShareResponse(false, req.userId, "인증 오류입니다.")

        val dataToSave = SharedUserEntity(
            userOrigin,
            userTarget,
            true
        )
        val entity = repo.findAllByUser_Account_UserIdAndTarget_Account_UserId(req.userId, user.username)
        if (entity.isNotEmpty()) {
            dataToSave.id = entity[0].id
        }
        repo.save(
            dataToSave
        )
        return AcceptShareResponse(true, req.userId, "공유받기에 성공하였습니다.")
    }

    fun addShare(user: UserAccountData, req: ShareToUserRequest, doShare: Boolean = false): ShareToUserResponse {
//        if (entity.isNotEmpty()) {
//            return ShareToUserResponse(req.userId, false, "이미 공유중이거나 공유 대기중인 유저입니다.")
//        }
        val userOrigin = userService.findAccountByUserId(user.username)
            ?: return ShareToUserResponse(req.userId, false, "계정 정보가 잘못되었습니다.")

        val userTarget = userService.findAccountByUserId(req.userId)
            ?: return ShareToUserResponse(req.userId, false, "대상 유저가 존재하지 않습니다.")
        val entity = repo.findAllByUser_Account_UserIdAndTarget_Account_UserId(user.username, req.userId)
        val dataToSave = SharedUserEntity(
            userOrigin,
            userTarget,
            doShare
        )
        if (entity.isNotEmpty()) {
            dataToSave.id = entity[0].id
        }
        repo.save(
            dataToSave
        )
        return ShareToUserResponse(req.userId, true, "공유에 성공하였습니다.")
    }

    fun getAllUsers(user: UserAccountData): ListUserResponse {
        val users = userService.findAllUser()
        val sharedUser =
            repo.findAllByUser_Account_UserId(user.username).associate { x -> x.target.account.userId to x.isShared }
        return ListUserResponse(
            users.map { x ->
                val userId = x.account.userId
                SharedUserData(
                    userId, x.name, x.phone, sharedUser[userId] ?: false
                )
            }
        )
    }


}