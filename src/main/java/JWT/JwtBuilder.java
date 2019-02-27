package JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.sql.Timestamp;
import java.util.Date;

public class JwtBuilder {
    private String key = "secret";
    public String create(String subject) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, key)
                .setSubject(subject)
                .claim("currTime", System.currentTimeMillis())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .compact();
    }
    public String checkSubject(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            return null;
        }
    }

}
