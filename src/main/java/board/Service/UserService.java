package board.Service;

import board.Domain.Entity.UserEntity;
import board.Domain.Repository.UserRepository;
import board.Domain.Role;
import board.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //==Spring Security 회원가입==//
    public Long join(UserDto userDto){

        //validateDuplicateMember(userDto);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDto.setPassword(encoder.encode(userDto.getPassword())); //encoding
        if(userDto.getEmail().equals("admin@example.com")){
            return userRepository.save(UserEntity.builder()
            .email(userDto.getEmail())
            .password(userDto.getPassword())
            .auth("ROLE_ADMIN,ROLE_USER").build()).getId(); //띄어쓰기 ㄴㄴ
        }
        else
            return userRepository.save(UserEntity.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .auth("ROLE_USER").build()).getId();
    }
//
//     //==중복 방지==//
//        private void validateDuplicateMember(UserDto userDto) {
//
//            if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
//                throw new IllegalStateException("이미 존재하는 회원입니다.");
//            }
//        }

    @Override
    public UserEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
