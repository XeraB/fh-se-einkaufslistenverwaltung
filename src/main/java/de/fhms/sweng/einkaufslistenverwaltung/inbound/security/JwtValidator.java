package de.fhms.sweng.einkaufslistenverwaltung.inbound.security;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.Role;
import de.fhms.sweng.einkaufslistenverwaltung.model.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class JwtValidator {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private PublicKeyProvider keys;

    /**
     * Retrieves JWT from HTTP-Header 'Authorization'.
     *
     * @param req
     * @return JWT as String withoud previx
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Checks whether a string is a valid JWT.
     *
     * @param token that should be checked
     * @return true, if the parsed String is a valid JWT
     */
    public boolean isValidJWT(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(keys.getPublicKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            LOG.info("Token rejected because " + e.getMessage());
            throw new ResourceNotFoundException("JWT invalid");
        }
    }

    /**
     * Derives Authentication object from JWT.
     *
     * @param token
     * @return an Object containing the email and the role of the user
     */
    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(getUserEmail(token), "", getRoles(token));
    }

    /**
     * Extracts the user's email from a JWT.
     *
     * @param token
     * @return the user email
     */
    public String getUserEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(keys.getPublicKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Extracts the user roles from a JWT.
     *
     * @param token
     * @return
     */
    public Collection<GrantedAuthority> getRoles(String token) {
        Collection<GrantedAuthority> result = new ArrayList<>();
        String temp = Jwts.parserBuilder().setSigningKey(keys.getPublicKey()).build().parseClaimsJws(token).getBody().get("auth", String.class);
        if (temp.equals("USER")) {
            result.add(Role.USER);
        } else if (temp.equals("PREMIUM")) {
            result.add(Role.PREMIUM);
        } else if (temp.equals("ADMIN")) {
            result.add(Role.ADMIN);
        }
        return result;
    }

}
