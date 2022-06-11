package kr.easw.drforestspringkt.util

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.SignatureException
import java.util.*
import javax.crypto.SecretKey
import kotlin.random.Random


object JwtUtil {
    val STRING_COLLECTION = "0123456789abcdefghijklmnopqrstuvwxyz_+-=~"
    private val jwtSecret = generateRandomTextSet(60)
    private val jwtRefreshSecret = generateRandomTextSet(60)
    private val jwtSecretKey: SecretKey =
        Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtSecret.toByteArray(StandardCharsets.UTF_8)))
    private val jwtRefreshSecretKey: SecretKey =
        Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtRefreshSecret.toByteArray(StandardCharsets.UTF_8)))

    // 1 Hours
    private val jwtExpire = 1 * 60 * 60 * 1000


    // 24 Hours
    private val jwtRefreshExpire = 24 * 60 * 60 * 1000

    fun generateToken(userName: String): String {
        val current = Date()
        val expire = Date(current.time + jwtExpire)
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(current)
            .setExpiration(expire)
            .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
            .compact()
    }

    fun generateRefreshToken(userName: String): String {
        val current = Date()
        val expire = Date(current.time + jwtRefreshExpire)
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(current)
            .setExpiration(expire)
            .signWith(jwtRefreshSecretKey, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUserFromToken(token: String): String {
        return Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token).body.subject
    }



    fun getUserFormRefreshToken(token: String): String {
        return Jwts.parserBuilder().setSigningKey(jwtRefreshSecretKey).build().parseClaimsJws(token).body.subject
    }


    // Jwt 토큰 유효성 검사
    fun validateToken(token: String): ValidateStatus {
        return try {
            Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token)
            ValidateStatus.VALID
        } catch (ex: MalformedJwtException) {
            ValidateStatus.INVALID
        } catch (ex: IllegalArgumentException) {
            ValidateStatus.INVALID
        } catch (ex: SignatureException) {
            ValidateStatus.INVALID
        } catch (ex: ExpiredJwtException) {
            ValidateStatus.EXPIRED
        } catch (ex: UnsupportedJwtException) {
            ValidateStatus.UNSUPPORTED
        }
    }


    // Jwt 토큰 유효성 검사
    fun validateRefreshToken(token: String): ValidateStatus {
        return try {
            Jwts.parserBuilder().setSigningKey(jwtRefreshSecretKey).build().parseClaimsJws(token)
            ValidateStatus.VALID
        } catch (ex: MalformedJwtException) {
            ex.printStackTrace()
            ValidateStatus.INVALID
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            ValidateStatus.INVALID
        } catch (ex: ExpiredJwtException) {
            ValidateStatus.EXPIRED
        } catch (ex: UnsupportedJwtException) {
            ValidateStatus.UNSUPPORTED
        }
    }

    private fun generateRandomTextSet(size: Int): String {
        val sb = StringBuilder()
        for (i in 0 until size) {
            sb.append(STRING_COLLECTION[Random.nextInt(STRING_COLLECTION.length)])
        }
        return sb.toString()
    }

    enum class ValidateStatus(val valid: Boolean) {
        VALID(true), INVALID(false), EXPIRED(false), UNSUPPORTED(false);
    }
}