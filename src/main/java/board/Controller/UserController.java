package board.Controller;


import board.Service.UserService;
import board.dto.UserDto;
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
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "home.html";
    }
    /**
     * 회원가입
     */
    @GetMapping("/user/new")
    public String MemberForm(){
        return "members/signup";
    }

    @PostMapping("/user/new")
    public String MemberJoin(UserDto userDto){
        userService.join(userDto);
        return "home.html";
    }

    /**
     * 로그인
     */
    @GetMapping("/user/login")
    public String MemberLoginForm(){
        return "members/login";
    }

    /**
     * 접근 금지 페이지
     */
    @GetMapping("/user/denied")
    public String dispDeny(){
        return "/denied";
    }
    /**
     * User 정보 페이지
     */
    @GetMapping("/user/info")
    public String dispMyInfo(){
        return "/members/myinfo";
    }
    /**
     * 어드민 페이지
     */
    @GetMapping("/admin")
    public String dispAdmin(){
        return "/admin";
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
