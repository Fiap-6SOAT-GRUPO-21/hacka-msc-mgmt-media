package br.com.fiap.mgmtmedia.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

public class JwtParser {

    public static String getIdentifyerUser() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || token.isBlank()) throw new RuntimeException("Token não informado");
        if (token.contains("Bearer ")) token = token.substring(7);
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();

        if (claims.get("sub") == null)
            throw new RuntimeException("sub deveria ser informado no token");

        return claims.get("sub").asString();
    }
}
