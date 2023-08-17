package it.polito.wa2.g17.server.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
class SecurityConfig {


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable() //TODO: cos'è il CSRF? Se è abilitato non funziona un cazzo... da approfondire
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/API/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/API/refresh").permitAll()
            .requestMatchers(HttpMethod.POST, "/API/signup").permitAll()
            .requestMatchers("/API/reset-password").authenticated()
            .requestMatchers("/API/manager/**").hasRole("MANAGER")
            .requestMatchers("API/expert/**").hasRole("EXPERT")
            .requestMatchers("/API/cashier/**").hasRole("CASHIER")
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
            val resourceAccess = jwt.getClaimAsMap("resource_access")
            val clientAccess = resourceAccess["wa2g17-keycloak-client"]
            val roles = clientAccess?.let { it1 -> (it1 as Map<String, Any>)["roles"] as List<String>}
                ?.map { "ROLE_${it}" }
            roles!!.map { GrantedAuthority({it}) }
        }
        converter.setPrincipalClaimName("email")
        return converter
    }
}