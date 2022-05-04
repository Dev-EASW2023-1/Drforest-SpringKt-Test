package kr.easw.drforestspringkt.service

import kr.easw.drforest.model.dto.ShareToUserRequest
import kr.easw.drforest.model.dto.ShareToUserResponse
import kr.easw.drforest.model.dto.SharedUserData
import kr.easw.drforest.model.dto.SharedUserListResponse
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.entity.SharedUserEntity
import kr.easw.drforestspringkt.model.repository.SharedUserRepository
import org.springframework.stereotype.Service

@Service
class SharedUserService(val repo: SharedUserRepository, val userService: AuthenticateService) {

    fun findAllUser(user: UserAccountData): SharedUserListResponse {
        return SharedUserListResponse(
            repo.findAllByUser_Account_UserId(user.username).map {
                SharedUserData(
                    it.user.account.userId,
                    it.user.name,
                    it.isShared
                )
            }
        )
    }

    fun addShare(user: UserAccountData, req: ShareToUserRequest): ShareToUserResponse {
        val entity = repo.findAllByUser_Account_UserIdAndTarget_Account_UserId(user.username, req.userId)
        if (entity.isPresent) {
            return ShareToUserResponse(req.userId, false, "이미 공유중이거나 공유 대기중인 유저입니다.")
        }
        val userOrigin = userService.findAccountByUserId(user.username)
            ?: return ShareToUserResponse(req.userId, false, "계정 정보가 잘못되었습니다.")

        val userTarget = userService.findAccountByUserId(user.username)
            ?: return ShareToUserResponse(req.userId, false, "대상 유저가 존재하지 않습니다.")
        repo.save(
            SharedUserEntity(
                userOrigin,
                userTarget,
                true
            )
        )
        return ShareToUserResponse(req.userId, true, "공유에 성공하였습니다.")
    }


}