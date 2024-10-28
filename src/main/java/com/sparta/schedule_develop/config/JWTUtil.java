package com.sparta.schedule_develop.config;

import ExceptionHandler.GlobalExceptionHandler;
import com.sparta.schedule_develop.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    // 토큰 만료시간
//    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    @Value("${jwt.token.expiration-time}")
    private long TOKEN_TIME;
    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

//    // JWT Cookie 에 저장
//    public void addJwtToCookie(String token, HttpServletResponse res) {
//        token = URLEncoder.encode(token, StandardCharsets.UTF_8).replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행
//
//        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
//        cookie.setPath("/");
//
//        // Response 객체에 Cookie 추가
//        res.addCookie(cookie);
//    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new IllegalArgumentException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(substringToken(token));
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("유효하지 않는 JWT 서명 입니다.");
            throw new GlobalExceptionHandler.InvalidTokenException("유효하지 않는 JWT 서명 입니다.");
            //logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("만료된 JWT token 입니다.");
            throw new GlobalExceptionHandler.ExpiredTokenException("만료된 JWT token 입니다.");
            //logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 토큰 입니다.");
            throw new GlobalExceptionHandler.UnsupportedTokenException("지원되지 않는 JWT 토큰 입니다.");
            //logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("잘못된 JWT 토큰 입니다.");
            throw new GlobalExceptionHandler.EmptyClaimsException("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            //logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        //return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        //return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token.substring(7)).getBody();
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(substringToken(token)).getBody();
    }
}