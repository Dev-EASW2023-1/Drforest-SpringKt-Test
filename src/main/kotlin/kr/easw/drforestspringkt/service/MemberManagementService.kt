package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.enumeration.Roles
import kr.easw.drforestspringkt.model.dto.DeleteUserRequest
import kr.easw.drforestspringkt.model.dto.DeleteUserResponse
import kr.easw.drforestspringkt.model.repository.*
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberManagementService(
    val authenticateService: AuthenticateService,
    val regionPermissionService: RegionPermissionService,
    val dailyUserDataRepository: DailyUserDataRepository,
    val qnARepository: QnARepository,
    val sharedUserRepository: SharedUserRepository,
    val userDataRepository: UserDataRepository,
    val userAccountRepository: UserAccountRepository,
    val userActivityDataRepository: UserActivityDataRepository,
    val userNoticeRepository: UserNoticeRepository
) {


    fun deleteUser(executor: UserAccountData, request: DeleteUserRequest): DeleteUserResponse {
        val executorEntity =
            authenticateService.findAccountByUserId(executor.username)
                ?: throw BadCredentialsException("관리자 데이터가 잘못되었습니다.")
        val targetUserEntity =
            authenticateService.findAccountByUserId(request.userId) ?: return DeleteUserResponse(
                false,
                "존재하지 않는 사용자입니다."
            )
        if (!Roles.listRoles(executorEntity.account.permission).contains(Roles.ADMIN)) {
            if (!regionPermissionService.isRegionManager(executorEntity, targetUserEntity.region)) {
                return DeleteUserResponse(false, "대상 유저에 접근할 권한이 존재하지 않습니다.")
            }
        }
        deleteUser(request.userId)
        return DeleteUserResponse(true, "사용자의 모든 데이터가 삭제되었습니다.")
    }

    @Transactional(readOnly = false)
    fun deleteUser(userId: String) {
        // 종속 관계의 끝부터 삭제한다.
        userNoticeRepository.deleteAllByUser_Account_UserId(userId)
//        userActivityDataRepository.deleteAllByEntity_UserId(userId)
//        dailyUserDataRepository.deleteAllByEntity_UserId(userId)
        qnARepository.deleteAllByUser_UserId(userId)
        sharedUserRepository.deleteAllByUser_Account_UserId(userId)
        sharedUserRepository.deleteAllByTarget_Account_UserId(userId)
        userDataRepository.deleteAllByAccount_UserId(userId)

        // 최종적으로 연결된 ID를 마지막에 삭제한다.
        userAccountRepository.deleteAllByUserId(userId)
    }
}