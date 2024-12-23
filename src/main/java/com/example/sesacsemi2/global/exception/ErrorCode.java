package com.example.sesacsemi2.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	// 일반적인 에러
	FAIL(HttpStatus.BAD_REQUEST, "실패 하였습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

	// 인증 관련 에러
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다."),

	// 사용자 관련 에러
	NOT_FOUND_USERNAME(HttpStatus.NOT_FOUND, "해당 아이디는 존재하지 않습니다."),
	ALREADY_USERNAME(HttpStatus.CONFLICT, "해당 아이디는 이미 사용 중입니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

	// S3 이미지 업로드 에러
	FILE_UPLOAD_ERROR(HttpStatus.PAYLOAD_TOO_LARGE, "파일 크기가 최대 제한을 초과하였습니다."),
	FILE_CONVERSION_ERROR(HttpStatus.BAD_REQUEST, "파일 변환에 실패하였습니다")
	;




	private final HttpStatus httpStatus;
	private final String msg;

	ErrorCode(HttpStatus httpStatus, String msg) {
		this.httpStatus = httpStatus;
		this.msg = msg;
	}
}
