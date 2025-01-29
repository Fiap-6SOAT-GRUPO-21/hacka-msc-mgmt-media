package br.com.fiap.mgmtmedia.utils;

import br.com.fiap.mgmtmedia.exception.custom.InvalidJwtException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@UtilityClass
public class JwtParser {

    public static String getUserIdentifier() {
        String token = getTokenFromAuthorizationHeader();

        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> claims = jwt.getClaims();

            if (claims.get("sub") == null) {
                throw new InvalidJwtException("'sub' claim is missing in the token");
            }

            return claims.get("sub").asString();
        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw new InvalidJwtException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    private static String getTokenFromAuthorizationHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new InvalidJwtException("No request context is available");
        }

        String authorizationHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new InvalidJwtException("Authorization header is missing");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidJwtException("Authorization header must start with 'Bearer '");
        }

        return authorizationHeader.substring(7);
    }

    public static String getPhoneNumber() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader(org.apache.http.HttpHeaders.AUTHORIZATION);

        if (token == null || token.isBlank()) throw new RuntimeException("Token não informado");
        if (token.contains("Bearer ")) token = token.substring(7);
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();

        if (claims.get("username") == null) {
            return null;
        }

        return claims.get("username").asString();
    }
}
