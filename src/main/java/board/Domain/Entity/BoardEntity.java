package board.Domain.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "board")
public class BoardEntity extends TimeEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "writer", nullable = false)
    private String writer;
    @Column(name = "views")
    private int viewcnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity userEntity;

    public void addView(){
        viewcnt++;
    }
    @Builder
    public BoardEntity(Long id, String title, String content, String writer, int viewcnt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.viewcnt = viewcnt;
    }

}
