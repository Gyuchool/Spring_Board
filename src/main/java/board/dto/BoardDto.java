package board.dto;

import board.Domain.Entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int viewcnt;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public BoardDto(BoardEntity boardEntity) {
        this.id = boardEntity.getId();
        this.title = boardEntity.getTitle();
        this.content = boardEntity.getContent();
        this.writer = boardEntity.getWriter();
        this.viewcnt= boardEntity.getViewcnt();
        this.createdDate = boardEntity.getCreatedDate();
        this.modifiedDate=boardEntity.getModifiedDate();
    }
}
