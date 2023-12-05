package com.example.tutorialwebflux.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;


    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(getKey(secret))
                .compact();
    }

    /*
    - generateToken: Este método toma un objeto UserDetails (que generalmente es la implementación de usuario proporcionada
      por Spring Security) y genera un token JWT.
    - Jwts.builder(): Inicia la construcción de un token JWT.
    - setSubject(userDetails.getUsername()): Establece el sujeto del token como el nombre de usuario del usuario autenticado.
    - claim("roles", userDetails.getAuthorities()): Agrega la información de roles al token.
      Los roles generalmente se incluyen como una "claim" en el token.
    - setIssuedAt(new Date()): Establece la fecha de emisión del token como la fecha actual.
    - setExpiration(new Date(new Date().getTime() + expiration * 1000)): Establece la fecha de vencimiento del token
      basada en la fecha actual más la duración de expiración proporcionada en segundos.
    - signWith(getKey(secret)): Firma el token utilizando una clave secreta. La clave secreta se obtiene mediante el método getKey.
    - compact(): Compacta el token en una cadena JWT válida.
    */

    public Claims getClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody();
    }

    public String getSubject (String token){
        return Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validate (String token){

        try{
            Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody();
            return true;
        }catch (ExpiredJwtException e){
            log.error("token expired");
        }catch (UnsupportedJwtException e){
            log.error("token unsupported");
        }catch (MalformedJwtException e){
            log.error("token malformed");
        }catch (SignatureException e) {
            log.error("bad signature");
        }catch (IllegalArgumentException e){
            log.error("illegal args");
        }
        return false;

    }




    private Key getKey(String key){
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);

    }

    /*
    getKey: Este método toma una cadena key (que generalmente es la clave secreta para firmar el token) y
    la convierte en una instancia de Key. En este caso, parece que la clave se está decodificando desde un formato base64
    y luego se utiliza para crear una clave HMAC (Hash-based Message Authentication Code)
    utilizando la clase Keys.hmacShaKeyFor.
    */






}
