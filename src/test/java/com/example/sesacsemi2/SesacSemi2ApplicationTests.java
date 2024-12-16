package com.example.sesacsemi2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SesacSemi2ApplicationTests {

	@Test
	void contextLoads() {
		// 기본 테스트 (성공)
	}

	@Test
	void intentionalFailureTest() {
		// 일부러 실패하는 테스트
		int expectedValue = 10;
		int actualValue = 5; // 실제 값이 기대 값과 다르게 설정됨
		assertEquals(expectedValue, actualValue, "This test is intentionally failing!");
	}
}
