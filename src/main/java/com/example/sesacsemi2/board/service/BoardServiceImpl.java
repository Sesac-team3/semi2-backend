package com.example.sesacsemi2.board.service;

import com.example.sesacsemi2.board.dto.BoardRequestDto;
import com.example.sesacsemi2.board.dto.BoardResponseDto;
import com.example.sesacsemi2.board.entity.Board;
import com.example.sesacsemi2.board.repository.BoardRepository;
import com.example.sesacsemi2.global.jwt.UserDetailsImpl;
import com.example.sesacsemi2.global.service.S3Service;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{

	private final BoardRepository boardRepository;
	private final S3Service s3Service;

	@Transactional
	public void createBoard(MultipartFile multipartFile, BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) throws IOException {
		String imageUrl = s3Service.upload(multipartFile);

		Board board = Board.builder()
			.image(imageUrl)
			.title(boardRequestDto.getTitle())
			.content(boardRequestDto.getContent())
			.user(userDetails.user())
			.build();

		boardRepository.save(board);
	}

	@Override
	public List<BoardResponseDto> getBoards(UserDetailsImpl userDetails) {
		return boardRepository.findAllById(userDetails.user().getId())
			.stream()
			.map(BoardResponseDto::new)
			.toList();
	}
}
