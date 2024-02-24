package com.itavgur.omul.appointment.config

import com.itavgur.omul.appointment.auth.JwtTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@ConditionalOnProperty("security.enabled", havingValue = "true", matchIfMissing = true)
class SecurityEnabled(
    @Lazy @Autowired private val jwtTokenFilter: JwtTokenFilter
) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {

        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

@Configuration
@EnableWebSecurity
@ConditionalOnProperty("security.enabled", havingValue = "false", matchIfMissing = false)
class SecurityDisabled {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {

        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .anyRequest().permitAll()
            }
            .sessionManagement { manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}