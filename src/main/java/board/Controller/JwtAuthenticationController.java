package board.Controller;

import board.Domain.Entity.MemberEntity;
import board.Service.JwtUserDetailsService;
import board.configuration.JwtTokenProvider;
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

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        final MemberEntity member = userDetailService.authenticateByEmailAndPassword
                (authenticationRequest.getEmail(), authenticationRequest.getPassword());
        final String token = JwtTokenProvider.generateToken(member.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Data
    class JwtRequest {

        private String email;
        private String password;

    }

    @Data
    @AllArgsConstructor
    class JwtResponse {

        private String token;

}

}
