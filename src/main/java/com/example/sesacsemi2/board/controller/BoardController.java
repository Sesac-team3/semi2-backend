package com.example.sesacsemi2.board.controller;

import com.example.sesacsemi2.board.dto.BoardRequestDto;
import com.example.sesacsemi2.board.dto.BoardResponseDto;
import com.example.sesacsemi2.board.service.BoardService;
import com.example.sesacsemi2.global.dto.CommonResponseDto;
import com.example.sesacsemi2.global.jwt.UserDetailsImpl;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class BoardController {

	private final BoardService boardService;

	@PostMapping("/boards")
	public ResponseEntity<CommonResponseDto<Void>> createBoard(
		@RequestPart(value = "boardImage") MultipartFile boardImage,
		@RequestPart(value = "boardRequestDto") @Valid BoardRequestDto boardRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) throws IOException {
		boardService.createBoard(boardImage, boardRequestDto, userDetails);
		return ResponseEntity.ok()
			.body(new CommonResponseDto<>(HttpStatus.CREATED.value(), "보드 생성 성공", null));
	}

	@GetMapping("/boards")
	public ResponseEntity<CommonResponseDto<List<BoardResponseDto>>> getBoards(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		List<BoardResponseDto> boardList = boardService.getBoards(userDetails);

		return ResponseEntity.ok()
			.body(new CommonResponseDto<>(HttpStatus.OK.value(), "보드 리스트", boardList));
	}
}
