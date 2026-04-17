package com.example.resonance.security

import com.example.resonance.database.dao.TokenBlacklistDao
import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.UserType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDao: UserDao,
    @Lazy private val tokenBlacklistDao: TokenBlacklistDao
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            try {
                if (!jwtUtil.validateAccessToken(token)) {
                    logger.warn("Invalid JWT token signature or structure")
                    filterChain.doFilter(request, response)
                    return
                }

                val tokenHash = jwtUtil.hashToken(token)
                if (tokenBlacklistDao.existsByTokenHash(tokenHash)) {
                    logger.warn("Token is blacklisted (user logged out)")
                    filterChain.doFilter(request, response)
                    return
                }

                val email = jwtUtil.extractEmail(token)
                if (email == null) {
                    logger.warn("Cannot extract email from token")
                    filterChain.doFilter(request, response)
                    return
                }

                val user = userDao.findByEmail(email)
                if (user == null) {
                    logger.warn("User not found: $email")
                    filterChain.doFilter(request, response)
                    return
                }

                if (!user.isActive) {
                    logger.warn("User account is deactivated: $email")
                    filterChain.doFilter(request, response)
                    return
                }

                val authentication = UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    getAuthorities(user.userType)
                )

                SecurityContextHolder.getContext().authentication = authentication

            } catch (e: Exception) {
                logger.error("Error processing JWT token: ${e.message}", e)
            }
        }

        filterChain.doFilter(request, response)
    }

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
