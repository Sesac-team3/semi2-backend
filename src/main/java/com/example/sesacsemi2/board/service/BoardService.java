package com.example.sesacsemi2.board.service;

import com.example.sesacsemi2.board.dto.BoardRequestDto;
import com.example.sesacsemi2.board.dto.BoardResponseDto;
import com.example.sesacsemi2.global.jwt.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

	@Transactional
	void createBoard(MultipartFile multipartFile,BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) throws IOException;

	List<BoardResponseDto> getBoards(UserDetailsImpl userDetails);
}
