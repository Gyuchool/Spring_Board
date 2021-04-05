package board.Controller;

import board.Domain.Entity.BoardEntity;
import board.Domain.Entity.UserEntity;
import board.Domain.Repository.BoardRepository;
import board.Domain.Repository.UserRepository;
import board.Service.UserService;
import board.dto.BoardDto;
import board.Service.BoardService;
import board.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    /* 게시글 목록 */
    @GetMapping("/list")
    public String list(Model model) {
        List<BoardDto> boardList = boardService.getBoardlist();

        model.addAttribute("boardList", boardList);

        return "board/list.html";
    }


    /* 게시글 상세 */
    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = boardService.getPost(no);

        boardService.addViews(no);
        model.addAttribute("boardDto", boardDTO);
        return "board/detail";
    }


    /* 게시글 쓰기 */
    @GetMapping("/post")
    public String write() {
        return "board/write";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        boardDto.setViewcnt(0);
        boardService.savePost(boardDto);

        return "redirect:/list";
    }


    /* 게시글 수정 */
    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = boardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/update";
    }

    @PostMapping("/post/edit/{no}")
    public String update(BoardDto boardDTO) {
        boardService.savePost(boardDTO);
        return "redirect:/list";
    }

    /* 게시글 삭제 */
    @PostMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) throws Exception {
        // == 수정 == //
        boardService.deletePost(no);
        return "redirect:/list";

    }
    /* 검색 */
    @GetMapping("/board/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/list";
    }
}

