package board.Controller;


import board.Service.UserService;
import board.dto.UserDto;
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

    private final UserService userService;

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
    public String MemberJoin(UserDto userDto){
        userDto.setAuth("USER");
        userService.join(userDto);
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
    public String MemberLogin(UserDto userDto){
         userService.login(userDto);
         return "/board/list";
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
