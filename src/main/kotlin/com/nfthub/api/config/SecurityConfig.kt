package com.nfthub.api.config

import com.nfthub.api.auth.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,

    ) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .cors().configurationSource {
                CorsConfiguration()
                    .apply {
                        allowedOriginPatterns = listOf("*")
                        allowedMethods = listOf("*")
                        allowCredentials = true
                        allowedHeaders = listOf("*")
                        exposedHeaders = listOf("Authorization")
                        maxAge = 3600L
                    }
            }
            .and().csrf().disable()
            .httpBasic().disable()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()


}