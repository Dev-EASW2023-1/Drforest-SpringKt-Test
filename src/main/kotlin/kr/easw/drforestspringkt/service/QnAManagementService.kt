package kr.easw.drforestspringkt.service

import kr.easw.drforest.model.dto.QnADataDto
import kr.easw.drforest.model.dto.QnAQuestionDto
import kr.easw.drforest.model.dto.QnaAnswerDto
import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.entity.QnAEntity
import kr.easw.drforestspringkt.model.repository.QnARepository
import org.springframework.stereotype.Service

@Service
class QnAManagementService(val qnARepository: QnARepository) {
    fun findQnA(user: UserAccountData): List<QnADataDto> {
        return qnARepository.getAllByUser_UserId(user.username).map { x ->
            QnADataDto(
                x.id!!.toInt(),
                QnAQuestionDto(
                    x.createdTime!!,
                    x.title,
                    x.content
                ),
                if (x.answerTitle != null) {
                    QnaAnswerDto(
                        x.updatedTime!!,
                        x.answerTitle!!,
                        x.answerContent!!
                    )
                } else {
                    null
                }
            )
        }
    }

}