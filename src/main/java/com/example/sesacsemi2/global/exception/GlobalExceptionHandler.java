package com.example.sesacsemi2.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponseDto> handleGlobalException(GlobalException ex) {
		log.error("Exception in {}: {}", ex.getErrorCode(), ex.getMessage());

		ErrorResponseDto errorResponse = ErrorResponseDto.builder()
														.status(ex.getErrorCode().getHttpStatus().value())
														.error(ex.getErrorCode().getHttpStatus().getReasonPhrase())
														.message(ex.getErrorCode().getMsg())
														.build();

		return ResponseEntity
			.status(ex.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleUnexpectedException(Exception ex) {
		log.error("Unexpected exception occurred: ", ex);

		ErrorResponseDto errorResponse = ErrorResponseDto.builder()
														.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
														.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
														.message("Unexpected error occurred. Please try again.")
														.build();

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(errorResponse);
	}
}
