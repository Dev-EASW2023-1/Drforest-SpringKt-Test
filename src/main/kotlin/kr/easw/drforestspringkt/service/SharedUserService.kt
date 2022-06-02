package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.*
import kr.easw.drforestspringkt.model.entity.SharedUserEntity
import kr.easw.drforestspringkt.model.repository.SharedUserRepository
import org.springframework.stereotype.Service

@Service
class SharedUserService(
    val repo: SharedUserRepository, val userService: AuthenticateService, val noticeService: UserNoticeService
) {

    fun findAllSharableUser(user: UserAccountData): SharableUserListResponse {
        val sharedUsers = repo.findAllByTarget_Account_UserId(user.username).associateBy { it.user.account.userId }
        val users = userService.findAllUser().map {
            SharableUserData(
                it.account.userId, it.name, it.phone, when {
                    sharedUsers.containsKey(it.account.userId) ->
                        if (sharedUsers[it.account.userId]!!.isShared) 1
                        else 2
                    else -> 0
                }
            )
        }
        return SharableUserListResponse(users)
    }

    // 내가 공유받은 목록
    fun findAllSharedUser(user: UserAccountData, onlyAccepted: Boolean = false): SharedUserListResponse {
        return SharedUserListResponse(repo.findAllByTarget_Account_UserId(user.username).map {
            SharedUserData(it.user.account.userId, it.user.name, it.user.phone, it.isShared)
        })
    }


    // 나에게 공유 요청을 한 목록
    fun findAllIncomingSharedUser(user: UserAccountData): SharedUserListResponse {
        return SharedUserListResponse(repo.findAllByUser_Account_UserId(user.username).map {
            SharedUserData(it.target.account.userId, it.target.name, it.target.phone, it.isShared)
        })
    }


    // Target -> User Share Accept Request
    fun acceptShare(user: UserAccountData, req: AcceptShareRequest): AcceptShareResponse {
        val userOrigin =
            userService.findAccountByUserId(user.username) ?: return AcceptShareResponse(false, req.userId, "인증 오류입니다.")

        val userTarget = userService.findAccountByUserId(req.userId) ?: return AcceptShareResponse(
            false,
            req.userId,
            "대상 유저는 공유를 신청하지 않았습니다."
        )

        val dataToSave = SharedUserEntity(
            userOrigin, userTarget, true
        )
        val entity = repo.findAllByUserAndTarget(userOrigin, userTarget)
        println("Origin Entity ${userOrigin.id}")
        println("Target Entity ${userTarget.id}")
        println("Target id ${req.userId}")
        if (entity.isNotEmpty()) {
            if (entity[0].isShared) {
                return AcceptShareResponse(true, req.userId, "이미 공유를 수락하였습니다.")
            }
            dataToSave.id = entity[0].id
        } else {
            // Impossible to occur
            return AcceptShareResponse(true, req.userId, "데이터가 존재하지 않지만, 수락 요청이 전송되었습니다. \n끔찍한 버그의 전조일까요?")
        }
        repo.save(
            dataToSave
        )
        noticeService.addNotice(user, "${userTarget.name}님에게 내 정보를 공유하였습니다.")
        noticeService.addNotice(userService.fromEntity(userTarget), "${userOrigin.name}님이 공유 요청을 수락하였습니다.")
        return AcceptShareResponse(true, req.userId, "대상 유저에게 데이터를 공유하였습니다.")
    }

    // User -> Target Share Request
    fun addShareWithNotice(
        user: UserAccountData, req: ShareToUserRequest, errorIfDuplicated: Boolean = false, doShare: Boolean = false
    ): ShareToUserResponse {
        val userOrigin = userService.findAccountByUserId(user.username) ?: return ShareToUserResponse(
            req.userId,
            false,
            "계정 정보가 잘못되었습니다."
        )

        val userTarget = userService.findAccountByUserId(req.userId) ?: return ShareToUserResponse(
            req.userId,
            false,
            "대상 유저가 존재하지 않습니다."
        )

        if (!doShare) {
            noticeService.addNotice(user, "${userTarget.name}님에게 공유 신청이 완료되었습니다.")
            noticeService.addNotice(userService.fromEntity(userTarget), "${userOrigin.name}님에게서 공유 요청이 도착하였습니다.")
        }
        return addShare(user, req, errorIfDuplicated, doShare)
    }

    // User -> Target Share Request
    fun addShare(
        user: UserAccountData,
        req: ShareToUserRequest,
        errorIfDuplicated: Boolean,
        doShare: Boolean = false
    ): ShareToUserResponse {
        val userOrigin = userService.findAccountByUserId(user.username) ?: return ShareToUserResponse(
            req.userId,
            false,
            "계정 정보가 잘못되었습니다."
        )

        val userTarget = userService.findAccountByUserId(req.userId) ?: return ShareToUserResponse(
            req.userId,
            false,
            "대상 유저가 존재하지 않습니다."
        )


        val entity = repo.findAllByUser_Account_UserIdAndTarget_Account_UserId(req.userId, user.username)
        val dataToSave = SharedUserEntity(
            userTarget, userOrigin, doShare
        )
        if (entity.isNotEmpty()) {
            if (errorIfDuplicated)
                return ShareToUserResponse(req.userId, false, "이미 공유 신청이 된 유저입니다.")
            dataToSave.id = entity[0].id
        }
        repo.save(
            dataToSave
        )
        return ShareToUserResponse(req.userId, true, "공유 신청 정보 업데이트에 성공하였습니다.")
    }

    fun getAllUsers(user: UserAccountData): ListUserResponse {
        val users = userService.findAllUser()
        val sharedUser =
            repo.findAllByUser_Account_UserId(user.username).associate { x -> x.target.account.userId to x.isShared }
        return ListUserResponse(users.map { x ->
            val userId = x.account.userId
            SharedUserData(
                userId, x.name, x.phone, sharedUser[userId] ?: false
            )
        })
    }

    fun findAllPendingUser(user: UserAccountData): PendingUserListResponse {
        return PendingUserListResponse(repo.findAllByTarget_Account_UserId(user.username).map {
            SharedUserData(
                it.target.account.userId, it.target.name, it.target.phone, it.isShared
            )
        })
    }

    fun addShareWithNotice(
        user: UserAccountData,
        req: ShareToUserWithPhoneNumberRequest
    ): ShareToUserWithPhoneNumberResponse {
        val target = userService.findUserByPhone(req.phoneNumber)
            ?: return ShareToUserWithPhoneNumberResponse(false, req.phoneNumber, "등록되지 않은 사용자입니다.")
        val result = addShareWithNotice(
            user, ShareToUserRequest(target.account.userId),
            errorIfDuplicated = true,
            doShare = false
        )
        return ShareToUserWithPhoneNumberResponse(result.success, result.userId, result.message)
    }

    fun cancelShare(user: UserAccountData, req: CancelShareRequest): CancelShareResponse {
        val entity = repo.findAllByUser_Account_UserIdAndTarget_Account_UserId(user.username, req.userId)
        if (entity.isEmpty()) {
            return CancelShareResponse(false, req.userId, "대상 유저는 공유를 신청하지 않았습니다.")
        }
        repo.delete(entity[0])
        return CancelShareResponse(true, req.userId, "공유를 취소하였습니다.")
    }

    fun cancelShareRequest(user: UserAccountData, req: CancelShareRequestRequest): CancelShareRequestResponse {
        val entity = repo.findAllByUser_Account_UserIdAndTarget_Account_UserId(req.userId, user.username)
        if (entity.isEmpty()) {
            return CancelShareRequestResponse(false, req.userId, "대상 유저에게 공유를 신청하지 않았습니다.")
        }
        repo.delete(entity[0])
        return CancelShareRequestResponse(true, req.userId, "공유를 취소하였습니다.")
    }


}