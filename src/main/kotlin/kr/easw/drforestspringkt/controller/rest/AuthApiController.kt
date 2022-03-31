package kr.easw.drforestspringkt.controller.rest

import kr.easw.drforestspringkt.model.dto.LoginDataDto
import kr.easw.drforestspringkt.model.dto.LoginDataResponse
import kr.easw.drforestspringkt.model.dto.RefreshTokenDto
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
    fun onLogin(@RequestBody data: LoginDataDto): ResponseEntity<LoginDataResponse> {
        return ResponseEntity.ok(authService.login(data))
    }

}