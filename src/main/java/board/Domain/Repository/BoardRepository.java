package board.Domain.Repository;

import board.Domain.Entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByTitleContaining(String keyword);

    List<BoardEntity> findAllByUserEntity(Long userId);
}
