package com.example.sesacsemi2.user.controller;

import com.example.sesacsemi2.global.dto.CommonResponseDto;
import com.example.sesacsemi2.user.dto.SignUpRequestDto;
import com.example.sesacsemi2.user.repository.UserRepository;
import com.example.sesacsemi2.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

	private final UserRepository userRepository;
	private final UserServiceImpl userService;


	@PostMapping("/signup")
	public ResponseEntity<CommonResponseDto<Void>> signup(
		@RequestBody @Valid SignUpRequestDto signUpRequestDto) {

		userService.signup(signUpRequestDto);

		return ResponseEntity.ok().body(new CommonResponseDto<>(
			HttpStatus.CREATED.value(), "회원가입이 완료되었습니다", null));
	}

}
