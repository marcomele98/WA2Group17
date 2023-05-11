package it.polito.wa2.g17.server.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import java.util.stream.Collectors


@Component
class JwtAuthConverter(properties: JwtAuthConverterProperties) : Converter<Jwt?, AbstractAuthenticationToken?> {
    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
    private val properties: JwtAuthConverterProperties

    init {
        this.properties = properties
    }

    fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities: Collection<GrantedAuthority> = Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(jwt)!!.stream(),
            extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet())
        return JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt))
    }

    private fun getPrincipalClaimName(jwt: Jwt): String {
        var claimName = JwtClaimNames.SUB
        if (properties.getPrincipalAttribute() != null) {
            claimName = properties.getPrincipalAttribute()
        }
        return jwt.getClaim(claimName)
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess: Map<String, Any> = jwt.getClaim("resource_access")
        var resource: Map<String?, Any?>
        var resourceRoles: Collection<String>
        return if (resourceAccess == null || resourceAccess[properties.getResourceId()] as kotlin.collections.MutableMap<kotlin.String?, kotlin.Any?>?. also {

            } == null || resource["roles"] as kotlin.collections.MutableCollection<kotlin.String?>?. also {
                resourceRoles = it
            } == null
        ) {
            setOf()
        } else resourceRoles.stream()
            .map { role: String ->
                SimpleGrantedAuthority(
                    "ROLE_$role"
                )
            }
            .collect(Collectors.toSet())
    }
}