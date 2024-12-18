package com.example.sesacsemi2.global.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "GlobalException")
@Getter
public class GlobalException extends RuntimeException {

	private final ErrorCode errorCode;

	public GlobalException(ErrorCode errorCode) {
		super(errorCode.getMsg());
		this.errorCode = errorCode;
	}
}
