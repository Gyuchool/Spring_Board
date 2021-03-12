package board.Controller;


import board.Service.MemberService;
import board.dto.MemberDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String home(Principal principal) {
        if(principal != null) {
            return "redirect:/list";
        }
        return "home.html";
    }
    /**
     * 회원가입
     */
    @GetMapping("/members/new")
    public String MemberForm(){
        return "members/signup";
    }

    @PostMapping("/members/new")
    public String MemberJoin(MemberDto memberDto){
        memberDto.setAuth("ROLE_USER");
        memberService.join(memberDto);
        return "redirect:/";
    }

    /**
     * 로그인
     */
    @GetMapping("/members/login")
    public String MemberLoginForm(){
        return "members/login";
    }

    @PostMapping("/members/login")
    public String MemberLogin(HttpServletRequest request, MemberDto memberDto){
        int login = memberService.login(memberDto);
        if(login == 1){
            return "redirect:/list";
        }
        return "redirect:/member/login";
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }
}
