package com.example.MyProjectWithSecurity.security.jwt;


import com.example.MyProjectWithSecurity.errs.MyJWTException;
import io.jsonwebtoken.*;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {

    @Value("${auth.secret}")
    private String secret;

    private String createToken(Map<String,Object> claims, String username){
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }

    public String getSecret() {
        return secret;
    }

    /**
     * Метод генерации токена
     * @param userDetails - параметры текущего пользователя
     * @return - сгенерированный токен jwt
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Возвращает свойства пользователя
     * @param token - jwt токен
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) throws MyJWTException {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    /**
     * Возвращает распарсеное значение токена
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) throws MyJWTException {
        Claims claims = null;
        try{
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            Logger.getLogger(JWTUtil.class.getSimpleName()).info("Exception ExpiredJwtException token is experation from extractAllClaims");
            throw new MyJWTException("token is not validation from extractAllClaims");
        }
        return claims;
    }

    /**
     * Возвращает имя юзера из токена
     * @param token
     * @return
     */
    public String extractUsername(String token) throws MyJWTException {
        String username = null;
        try {
             username = extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException e){
            Logger.getLogger(JWTUtil.class.getSimpleName()).info("Exception ExpiredJwtException token is experation from extractUsername");
            throw new MyJWTException("token is not validation from extractUsername");
        }
        return username;
    }

    /**
     * Возвращает срок годности токена
     * @param token
     * @return
     */
    public Date extractExpiration(String token) throws MyJWTException {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Возвращает информацию boolean истек срок годности токена или нет
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) throws MyJWTException {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Возвращает информацию boolean о валидности или невалидности токена.
     * Проверяет соответствие имени пользователя в токене и в текущем userDetails
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) throws MyJWTException {
        boolean validation = false;
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            validation = true;
        }catch (ExpiredJwtException e){
            Logger.getLogger(JWTUtil.class.getSimpleName()).info("Exception ExpiredJwtException token is experation from validateToken");
            throw new MyJWTException("token is not validation from validateToken");
        } catch (SignatureException ex){
            ex.printStackTrace();
        } catch (Exception exe){
            exe.printStackTrace();
        }
            /*String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));*/

        return validation;
    }
}
