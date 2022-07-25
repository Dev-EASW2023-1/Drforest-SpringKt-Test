package kr.easw.drforestspringkt.controller.rest.v1

import io.swagger.v3.oas.annotations.Operation
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
@RequestMapping("/api/v1/auth")
class AuthApiController(
    val authService: AuthenticateService,
) {
    @PostMapping("/login")
    @Tag(name = "인증 API", description = "ID와 PW, 혹은 토큰을 기반으로 인증을 진행합니다.")
    @Operation(summary = "로그인 API")
    fun onLogin(@RequestBody data: LoginDataRequest): ResponseEntity<LoginDataResponse> {
        return ResponseEntity.ok(authService.login(data))
    }

}