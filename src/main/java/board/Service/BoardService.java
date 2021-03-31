package board.Service;

import board.Domain.Entity.BoardEntity;
import board.Domain.Repository.BoardRepository;
import board.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public List<BoardDto> getBoardlist() {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        boardEntities.stream().forEach(boardEntity -> {
            boardDtoList.add(new BoardDto(boardEntity));
        });

        /*
        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }
        */

        return boardDtoList;
    }

    @Transactional
    public Long getBoardCount() {
        return boardRepository.count();
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        BoardEntity boardEntity = boardEntityWrapper.get();

        return new BoardDto(boardEntity);
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {

        return boardRepository.save(BoardEntity.builder()
            .id(boardDto.getId())
            .title(boardDto.getTitle())
        .content(boardDto.getContent())
        .viewcnt(boardDto.getViewcnt())
        .writer(boardDto.getWriter())
        .build()).getId();
    }
    @Transactional
    public void addViews(Long boardId){
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        boardEntity.addView();

    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public List<BoardDto> searchPosts(String keyword) {
        List<BoardEntity> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if (boardEntities.isEmpty()) return boardDtoList;

        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(new BoardDto(boardEntity));
        }

        return boardDtoList;
    }

}