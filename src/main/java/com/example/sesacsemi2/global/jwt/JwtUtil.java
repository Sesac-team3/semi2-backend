package com.example.sesacsemi2.global.jwt;

import com.example.sesacsemi2.user.UserRole;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {


	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String REFRESH_TOKEN = "Refresh";
	public static final String AUTHORIZATION_KEY = "auth";
	public static final String BEARER_PREFIX = "Bearer";

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
}
