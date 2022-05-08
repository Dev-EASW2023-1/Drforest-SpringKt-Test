package kr.easw.drforestspringkt.service

import kr.easw.drforestspringkt.auth.UserAccountData
import kr.easw.drforestspringkt.model.dto.QnADataData
import kr.easw.drforestspringkt.model.dto.QnADataResponse
import kr.easw.drforestspringkt.model.dto.QnAQuestionData
import kr.easw.drforestspringkt.model.dto.QnaAnswerData
import kr.easw.drforestspringkt.model.repository.QnARepository
import org.springframework.stereotype.Service

@Service
class QnAManagementService(val qnARepository: QnARepository) {
    fun findQnA(user: UserAccountData): QnADataResponse {
        return QnADataResponse(
            qnARepository.getAllByUser_UserId(user.username).map {
                x -> QnADataData(
                        x.id!!.toInt(),
                        QnAQuestionData(
                            x.createdTime!!,
                            x.title,
                            x.content
                        ),
                    if (x.answerTitle != null) {
                        QnaAnswerData(
                            x.updatedTime!!,
                            x.answerTitle!!,
                            x.answerContent!!
                        )
                    } else {
                        null
                    }
                )
            }
        )
    }
}