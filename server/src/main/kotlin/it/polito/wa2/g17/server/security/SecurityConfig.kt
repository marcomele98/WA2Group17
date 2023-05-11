package it.polito.wa2.g17.server.security

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable() //TODO: cos'è il CSRF? Se è abilitato non funziona un cazzo... da approfondire
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/API/login").permitAll()
            .requestMatchers("/API/manager/**").hasRole("MANAGER")
            .requestMatchers("API/expert/**").hasRole("EXPERT")
            .requestMatchers("/API/customer/**").hasRole("CLIENT")
            .anyRequest().authenticated();
        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build()
    }


    @Bean
    fun jwtAuthConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt: Jwt ->
            jwt
                .getClaim<String>("roles")
                .split(",")
                .map { GrantedAuthority { it } }
        }
        return converter
    }


}