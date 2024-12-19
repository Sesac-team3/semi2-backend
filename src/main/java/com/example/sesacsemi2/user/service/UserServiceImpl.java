package com.example.sesacsemi2.user.service;

import com.example.sesacsemi2.global.exception.ErrorCode;
import com.example.sesacsemi2.global.exception.GlobalException;
import com.example.sesacsemi2.global.jwt.JwtUtil;
import com.example.sesacsemi2.user.dto.LoginRequestDto;
import com.example.sesacsemi2.user.dto.SignUpRequestDto;
import com.example.sesacsemi2.user.entity.UserRole;
import com.example.sesacsemi2.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.sesacsemi2.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	@Transactional
	public void signup(SignUpRequestDto signUpRequestDto) {
		checkUsername(signUpRequestDto);
		User user = createUser(signUpRequestDto);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
		User user = validateLoginRequest(loginRequestDto);
		String refreshToken = jwtUtil.issueToken(user, response);
		String refreshTokenKey = "refreshToken:" + user.getId();
		redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, 30, TimeUnit.DAYS);
	}

	private User validateLoginRequest(LoginRequestDto loginRequestDto) {
		User user = userRepository.findByUsername(loginRequestDto.getUsername())
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USERNAME));
		validateUserPassword(loginRequestDto.getPassword(), user.getPassword());
		return user;
	}

	private void validateUserPassword(String rawPassword, String encodedPassword) {
		if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new GlobalException(ErrorCode.INVALID_PASSWORD);
		}
	}

	private User createUser(SignUpRequestDto signUpRequestDto) {
		return User.builder()
			.username(signUpRequestDto.getUsername())
			.password(passwordEncoder.encode(signUpRequestDto.getPassword()))
			.userRole(UserRole.USER)
			.build();

	}

	private void checkUsername(SignUpRequestDto signUpRequestDto) {
		if(userRepository.findByUsername(signUpRequestDto.getUsername()).isPresent()) {
			throw new GlobalException(ErrorCode.ALREADY_USERNAME);
		}

	}
}
