package com.example.sesacsemi2.board.repository;

import com.example.sesacsemi2.board.entity.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

	List<Board> findAllById(Long id);
}
