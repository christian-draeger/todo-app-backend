package com.example.demo.config

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.factory.PasswordEncoderFactories

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http {
            csrf { disable() }
            httpBasic {}
            authorizeRequests {
                authorize("/todo", hasAuthority("ROLE_ADMIN"))
                authorize("/todo/**", hasAuthority("ROLE_USER"))
                authorize("/**", permitAll)
            }
        }
    }

    private val String.encoded
        get() = PasswordEncoderFactories
            .createDelegatingPasswordEncoder()
            .encode(this)

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .inMemoryAuthentication()
            .withUser("kigali-coder")
            .password("pa55w0rd".encoded)
            .roles("USER")
            .and()
            .withUser("admin")
            .password("password".encoded)
            .roles("ADMIN", "USER")
    }

}
