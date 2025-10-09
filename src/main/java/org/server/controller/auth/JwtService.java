package org.server.controller.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.server.config.shared.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service //- Toca pensar como hacer esto xd
public class JwtService {
    private static final Logger logger = Logger.getLogger(JwtService.class.getName());

    private static final String SECRET = "zG58uT_4o-kxXwO8ZC6Ly9jEkTbnQNg7cIyx8Y8E28I="; // Esto se debería hacer en secreto claramente xd, solo es ejemplificando.
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long expirationTime = 7 * 24 * 60 * 60 * 1000; // 7 dias

    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY) // aqui la firmamos
                .compact();
    }

    // esta es la validacion de toda la vida xd
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            logger.log(Level.WARNING, " Error en a validación del token: {} ", e);
            return false;
        }
    }

    // esto es por norma de obtener el username si algo, pero pss,, creo que se puede quitar
    public static String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
