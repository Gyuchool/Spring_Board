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
            boardDtoList.add(convertEntityToDto(boardEntity));
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

        return this.convertEntityToDto(boardEntity);
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {

        return boardRepository.save(boardDto.toEntity()).getId();
    }
    @Transactional
    public Long addViews(BoardDto boardDto){
        int view = boardDto.getViewcnt();
        boardDto.setViewcnt(view+1);
        return boardRepository.save(boardDto.toEntity()).getId();
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
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        return BoardDto.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .viewcnt(boardEntity.getViewcnt())
                .createdDate(boardEntity.getCreatedDate())
                .modifiedDate(boardEntity.getModifiedDate())
                .build();
    }
}