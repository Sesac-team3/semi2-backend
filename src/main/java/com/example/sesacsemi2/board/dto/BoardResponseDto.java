package com.example.sesacsemi2.board.dto;


import com.example.sesacsemi2.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {

	private final Long id;
	private final String image;
	private final String content;


	public BoardResponseDto(Board board) {
		this.id = board.getId();
		this.image = board.getImage();
		this.content = board.getContent();
	}
}
