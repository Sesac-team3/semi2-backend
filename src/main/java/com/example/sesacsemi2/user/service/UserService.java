package com.example.sesacsemi2.user.service;

import com.example.sesacsemi2.global.exception.ErrorCode;
import com.example.sesacsemi2.global.exception.GlobalException;
import com.example.sesacsemi2.global.jwt.JwtUtil;
import com.example.sesacsemi2.user.dto.SignUpRequestDto;
import com.example.sesacsemi2.user.entity.UserRole;
import com.example.sesacsemi2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.sesacsemi2.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceImpl{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	@Transactional
	public void signup(SignUpRequestDto signUpRequestDto) {
		checkUsername(signUpRequestDto);
		User user = createUser(signUpRequestDto);
		userRepository.save(user);
	}

	private User createUser(SignUpRequestDto signUpRequestDto) {
		return User.builder()
			.username(signUpRequestDto.getUsername())
			.password(signUpRequestDto.getPassword())
			.userRole(UserRole.USER)
			.build();

	}

	private void checkUsername(SignUpRequestDto signUpRequestDto) {
		if(userRepository.findByUsername(signUpRequestDto.getUsername()).isPresent()) {
			throw new GlobalException(ErrorCode.ALREADY_USERNAME);
		}

	}
}
