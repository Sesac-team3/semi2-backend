package com.example.sesacsemi2.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {

	private int status;
	private String error;
	private String message;
}
