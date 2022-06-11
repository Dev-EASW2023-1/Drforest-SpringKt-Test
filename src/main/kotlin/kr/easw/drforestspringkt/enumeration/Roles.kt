package kr.easw.drforestspringkt.enumeration

import kr.easw.drforestspringkt.util.checkFlag
import kr.easw.drforestspringkt.util.negative
import kr.easw.drforestspringkt.util.positive
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

// Permission level can be duplicated, and will be reduced to one integer value.
enum class Roles : GrantedAuthority {
    API,
    USER,
    MANAGER,
    ADMIN
    ;

    companion object {
        fun listRoles(origin: Int): List<Roles> {
            return values().filter { role -> checkFlag(origin, role.ordinal) }
        }

        fun allow(vararg roles: Roles): Int {
            var allowed = 0
            for (role in roles) {
                allowed = role.allow(allowed)
            }
            return allowed
        }
    }

    override fun getAuthority(): String {
        return "ROLE_$name"
    }

    fun allow(origin: Int): Int {
        return origin positive ordinal
    }

    fun disallow(origin: Int): Int {
        return origin negative ordinal
    }


}