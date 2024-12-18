package com.example.sesacsemi2.global.jwt;

import com.example.sesacsemi2.global.exception.ErrorCode;
import com.example.sesacsemi2.global.exception.GlobalException;
import com.example.sesacsemi2.user.entity.User;
import com.example.sesacsemi2.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String REFRESH_TOKEN = "Refresh";
	public static final String AUTHORIZATION_KEY = "auth";
	public static final String BEARER_PREFIX = "Bearer";
	private static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

	@Value("${jwt.secret-key}")
	String secretkey;

	@Value("${jwt.access-expire-time}")
	long accessTokenExpireTime;

	@Value("${jwt.refresh-expire-time}")
	long refreshTokenExpireTime;

	private Key key;

	final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretkey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createAccessToken(String userName, UserRole userRole) {
		return createToken(userName, userRole, accessTokenExpireTime);
	}

	public String createRefreshToken(String userName) {
		return createToken(userName, null, refreshTokenExpireTime);
	}

	private String createToken(String userName, UserRole userRole, long expireTime) {
		Date now = new Date();

		JwtBuilder builder = Jwts.builder()
			.setSubject(userName)
			.setExpiration(new Date(now.getTime() + expireTime))
			.setIssuedAt(now)
			.signWith(key, signatureAlgorithm);

		if (userRole != null) {
			builder.claim(AUTHORIZATION_KEY, userRole);
		}

		return builder.compact();
	}

	public void addJwtToHeader(String headerName, String token, HttpServletResponse response) {
		response.addHeader(headerName, token);
	}

	public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
			.path("/")
			.sameSite("None")
			.httpOnly(true)
			.secure(false)
			.maxAge((int) refreshTokenExpireTime)
			.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}

	public String issueToken(User user, HttpServletResponse response) {
		String accessToken = createAccessToken(user.getUsername(), user.getUserRole());
		String refreshToken = createRefreshToken(user.getUsername());

		addJwtToHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken, response);
		addRefreshTokenCookie(response, refreshToken);

		return accessToken;
	}

	public  boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);

			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException ignored) {
			logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException ignored) {
			logger.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException ignored) {
			logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
		}catch (IllegalArgumentException ignored) {
			logger.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
		}
		return false;
	}

	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public String refreshAccessToken(String refreshToken) {
		if(validateToken(refreshToken)) {
			Claims claims = getUserInfoFromToken(refreshToken);
			String username = claims.getSubject();
			UserRole userRole = UserRole.valueOf(claims.get(AUTHORIZATION_KEY, String.class));

			return createAccessToken(username, userRole);
		}
		throw new GlobalException(ErrorCode.INVALID_REFRESH_TOKEN);
	}
}
