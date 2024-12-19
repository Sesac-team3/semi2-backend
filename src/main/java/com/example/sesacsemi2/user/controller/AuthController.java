package com.example.sesacsemi2.user.controller;

import com.example.sesacsemi2.global.dto.CommonResponseDto;
import com.example.sesacsemi2.user.dto.LoginRequestDto;
import com.example.sesacsemi2.user.dto.SignUpRequestDto;
import com.example.sesacsemi2.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
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

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<CommonResponseDto<Void>> signup(
		@RequestBody @Valid SignUpRequestDto signUpRequestDto) {

		userService.signup(signUpRequestDto);

		return ResponseEntity.ok().body(new CommonResponseDto<>(
			HttpStatus.CREATED.value(), "회원가입이 완료되었습니다", null));
	}

	@PostMapping("/login")
	public ResponseEntity<CommonResponseDto<Void>> login(
		@RequestBody @Valid LoginRequestDto loginRequestDto,
		HttpServletResponse httpServletResponse
	) {
		userService.login(loginRequestDto, httpServletResponse);

		return ResponseEntity.ok().body(new CommonResponseDto<>(
			HttpStatus.OK.value(), "로그인이 완료되었습니다", null
		));
	}

}
