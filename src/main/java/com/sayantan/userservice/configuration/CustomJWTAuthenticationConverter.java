package com.sayantan.userservice.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomJWTAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private String principalClaimName = "sub";

    public CustomJWTAuthenticationConverter() {
    }

    public final AbstractAuthenticationToken convert(Jwt jwt) {
        //Collection<GrantedAuthority> authorities = (Collection)this.jwtGrantedAuthoritiesConverter.convert(jwt);
        Collection<GrantedAuthority> authorities = Stream.concat(this.jwtGrantedAuthoritiesConverter.convert(jwt).stream(),this.getKeyCloakRole(jwt).stream()).collect(Collectors.toSet());
        String principalClaimValue = jwt.getClaimAsString(this.principalClaimName);
        return new JwtAuthenticationToken(jwt, authorities, principalClaimValue);
    }

    public void setJwtGrantedAuthoritiesConverter(Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
    }

    public void setPrincipalClaimName(String principalClaimName) {
        Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
        this.principalClaimName = principalClaimName;
    }

    private Collection<GrantedAuthority> getKeyCloakRole(Jwt jwt){
        Map<String,Object> resourceAccess;
        Map<String,Object> roles;
        resourceAccess = jwt.getClaim("resource_access");
        if(resourceAccess==null){
            return Set.of();
        }
        roles= (Map<String, Object>) resourceAccess.get("developer");
        if(roles==null){
            return Set.of();
        }
        Collection<String> resourceRoles = (Collection<String>) roles.get("roles");

        return resourceRoles.stream().map(str->new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toSet());
    }
}
