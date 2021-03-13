package board.Controller.api;

import board.Domain.Entity.UserEntity;
import board.Service.JwtUserDetailsService;
import board.Service.UserService;
import board.configuration.JwtTokenProvider;
import board.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final JwtTokenProvider JwtTokenProvider;
    private final JwtUserDetailsService userDetailService;
    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        final UserEntity member = userDetailService.authenticateByEmailAndPassword
                (authenticationRequest.getEmail(), authenticationRequest.getPassword());
        final String token = JwtTokenProvider.generateToken(member.getEmail());
        System.out.println(token);
        return ResponseEntity.ok(new JwtResponse(token));
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
    @PostMapping("/api/member")
    public JwtResponse singup(@RequestBody JwtRequest jwtRequest){
        UserEntity userEntity = userDetailService.authenticateByEmailAndPassword(jwtRequest.getEmail(), jwtRequest.getPassword());
        userService.join(new UserDto(userEntity));
        String s = JwtTokenProvider.generateToken(jwtRequest.getEmail());
        return new JwtResponse(s);
    }

}
