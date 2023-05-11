package it.polito.wa2.g17.server.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
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
class SecurityConfig {

  /*  val secretKey =
    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArV5IsU4BeMwyyTVRDtVzLIkOQz9JbJADyWfWF48y8OriHu1wmexzXWLT/NjjxPToxHmrjFWT+vrbC7ePv6L6R5qLpk0IpUCCPUXTiYL70GKW58Ual8G8sjyurWM8/5zrT+HUr50GSkh7MRfnvYY8zOjVSKCWAvH/lM/1ddxbI/BfCVm/6y7DU8KOHBI46I9lW5wmzLuGylgrTmNDhl2nsD7kjH4XZiUC47Xy5NCsHUiNAf8sgB/k/q/BwwiBN/yKll9PkteYdVvEItNCJCcIPew4/byaaAPRoTKecpzm+/4HuQyU0goOeja+HnyqjPwwE9Rg+Tp0sWzKG6HYWSBUBwIDAQAB"
*/

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable() //TODO: cos'è il CSRF? Se è abilitato non funziona un cazzo... da approfondire
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/API/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/API/profiles").permitAll()
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

/*
    @Bean
    fun jwtDecoder(): JwtDecoder {
        val key = SecretKeySpec(secretKey.toByteArray(), "RSASHA256")
        return NimbusJwtDecoder
            .withSecretKey(key)
            .build()
    }
*/



    @Bean
    fun jwtAuthConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt: Jwt ->
            val resourceAccess = jwt.getClaimAsMap("resource_access")
            val clientAccess = resourceAccess["wa2g17-keycloak-client"]
            val roles = clientAccess?.let { it1 -> (it1 as Map<String, Any>)["roles"] as List<String> }
            roles!!.map { GrantedAuthority { it } }
        }
        println(converter)
        return converter
    }

}