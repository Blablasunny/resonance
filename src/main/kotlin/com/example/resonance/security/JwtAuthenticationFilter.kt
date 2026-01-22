package com.example.resonance.security

import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.UserType
import com.example.resonance.service.AuthService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDao: UserDao
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            if (jwtUtil.validateToken(token)) {
                try {
                    val user = getUserFromToken(token)
                    val authentication = UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        getAuthorities(user.userType)
                    )

                    SecurityContextHolder.getContext().authentication = authentication
                } catch (e: Exception) {
                    logger.error("Error processing JWT token", e)
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getUserFromToken(token: String) =
        userDao.findByEmail(jwtUtil.extractUsername(token))
            ?: throw org.springframework.security.core.userdetails.UsernameNotFoundException(
                "User not found"
            )

    private fun getAuthorities(userType: UserType): Collection<GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority("ROLE_USER"))

        when (userType) {
            UserType.STUDENT -> authorities.add(SimpleGrantedAuthority("ROLE_STUDENT"))
            UserType.COMPANY -> authorities.add(SimpleGrantedAuthority("ROLE_COMPANY"))
        }

        return authorities
    }
}