package kr.easw.drforestspringkt.controller.rest

import io.swagger.v3.oas.annotations.tags.Tag
import kr.easw.drforestspringkt.model.dto.LoginDataRequest
import kr.easw.drforestspringkt.model.dto.LoginDataResponse
import kr.easw.drforestspringkt.service.AuthenticateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthApiController(
    val authService: AuthenticateService,
) {
    @PostMapping("/login")
    @Tag(name = "인증 API", description = "ID와 PW를 기반으로 로그인을 진행합니다.")
    fun onLogin(@RequestBody data: LoginDataRequest): ResponseEntity<LoginDataResponse> {
        return ResponseEntity.ok(authService.login(data))
    }

}