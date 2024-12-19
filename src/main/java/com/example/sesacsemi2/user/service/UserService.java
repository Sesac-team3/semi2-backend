package com.example.sesacsemi2.user.service;


import com.example.sesacsemi2.user.dto.LoginRequestDto;
import com.example.sesacsemi2.user.dto.SignUpRequestDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

	void signup(SignUpRequestDto signUpRequestDto);

	void login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse);
}
