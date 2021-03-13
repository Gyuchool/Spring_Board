package board.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private static final String secret = "jwtpassword"; //수정 필요
    // 토큰 유효 기간
    public static final long JWT_TOKEN_VALIDITY = 60 * 60 * 24 * 1000L; //하루

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    // 토큰에서 회원 정보 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody();// JWT payload 에 저장되는 정보단위
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    //JWT 토큰 생성1
    public String generateToken(String id) {
        return doGenerateToken(id, new HashMap<>());
    }

    //JWT 토큰 생성2
    private String doGenerateToken(String id, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)  // 정보 저장
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 발행 시간 정보
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // set Expire Time
                .signWith(SignatureAlgorithm.HS512, secret)// 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}