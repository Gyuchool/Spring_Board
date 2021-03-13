package board.Service;

import board.Domain.Entity.UserEntity;
import board.Domain.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
//        // 권한 //
//        UserEntity member = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException(email));
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
//        if (email.equals("giron1210@naver.com")) {  //내 이메일만 admin
//            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
//        }
//
//        return new User(member.getEmail(), member.getPassword(), grantedAuthorities);
//
//        // 인증 //
//        public UserEntity authenticateByEmailAndPassword(String email, String password) {
//            UserEntity member = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException(email));
//
//            if(!passwordEncoder.matches(password, member.getPassword())) {
//                throw new BadCredentialsException("Password not matched");
//            }
//
//            return member;
//        }

}
