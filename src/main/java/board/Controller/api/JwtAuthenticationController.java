package board.Controller.api;

import board.Domain.Entity.UserEntity;
import board.Domain.Repository.UserRepository;
import board.Service.CustomUserDetailsService;
import board.Service.UserService;
import board.configuration.JwtTokenProvider;
import board.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody JwtRequest user) {
        return userRepository.save(UserEntity.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .auth("ROLE_USER") // 최초 가입시 USER 로 설정
                .build()).getId();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        UserEntity member = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.generateToken(member.getUsername());
    }

    //회원가입이 되고 이 api를 보내면 토큰을 받을 수 있다.
    @PostMapping("/authenticate")
    public String createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        }
        catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtTokenProvider.generateToken(authenticationRequest.getEmail());

    }
    @Data
    private static class JwtRequest {

        private String email;
        private String password;

    }
    @Data
    @AllArgsConstructor
    private static class JwtResponse {

        private String token;

    }

}
