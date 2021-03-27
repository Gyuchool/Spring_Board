package board.dto;

import board.Domain.Entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int viewcnt;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public BoardEntity toEntity(){
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .viewcnt(viewcnt)
                .build();
        return boardEntity;
    }

    public BoardDto(Long id, String title, String content, String writer, int viewcnt, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.viewcnt=viewcnt;
        this.createdDate = createdDate;
        this.modifiedDate=modifiedDate;
    }
}
