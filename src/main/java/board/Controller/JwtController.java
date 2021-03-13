//package board.Controller;
//
//import board.Domain.Entity.UserEntity;
//import board.Service.UserService;
//import board.Service.UserService;
//import board.configuration.JwtTokenProvider;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Required;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@Controller
//@RequiredArgsConstructor
//public class JwtController {
//
//    private final board.configuration.JwtTokenProvider JwtTokenProvider;
//    private final UserService userDetailService;
//    private final UserService userService;
//
//    @PostMapping("/authenticate")
//    public ResponseEntity<?> createAuthenticationToken( JwtRequest authenticationRequest) throws Exception {
//
//        final UserEntity member = userDetailService.authenticateByEmailAndPassword
//                (authenticationRequest.getEmail(), authenticationRequest.getPassword());
//        final String token = JwtTokenProvider.generateToken(member.getEmail());
//
//
//    }
//
//    @Data
//    @AllArgsConstructor
//    private static class JwtRequest {
//        private String email;
//        private String password;
//    }
//}
