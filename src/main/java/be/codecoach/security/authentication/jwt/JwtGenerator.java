package be.codecoach.security.authentication.jwt;

import be.codecoach.domain.RoleEnum;
import be.codecoach.security.authentication.user.api.Account;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class JwtGenerator {
    private static final int TOKEN_TIME_TO_LIVE = 3600000;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtGenerator.class);

    private final String jwtSecret;

    public JwtGenerator(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public UsernamePasswordAuthenticationToken convertToken(String token) {
        LOGGER.info("convertToken: ");
        if (isEmpty(token)) {
            return null;
        }
        LOGGER.info("convertToken: checked for null token");
        Jws<Claims> parsedToken = null;
        try {
            parsedToken = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token);
        } catch (ExpiredJwtException exception) {
            LOGGER.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            LOGGER.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            LOGGER.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            LOGGER.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            LOGGER.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }
        LOGGER.info("convertToken: JWS<Claims>");
        if (parsedToken == null) {
            return null;
        }
        LOGGER.info("convertToken: checked parsedToken for null");

        var username = parsedToken.getBody().getSubject();

        LOGGER.info("convertToken: var username =" + username);

        List<String> authoritiesInToken = parsedToken.getBody().get("role", ArrayList.class);
        var authorities = authoritiesInToken.stream()
                .map(RoleEnum::valueOf)
                .collect(Collectors.toList());
        LOGGER.info("convertToken: authorities =" + authorities);

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public String generateToken(Account account) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .setIssuer("secure-api")
                .setAudience("secure-app")
                .setSubject(account.getEmail())
                .setExpiration(new Date(new Date().getTime() + TOKEN_TIME_TO_LIVE))
                .claim("role", account.getAuthorities())
                .claim("id", account.getId())
                .compact();
    }
}
