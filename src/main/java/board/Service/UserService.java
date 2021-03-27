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

        validateDuplicateMember(userDto);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDto.setPassword(encoder.encode(userDto.getPassword()));

        return userRepository.save(UserEntity.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .auth("ROLE_USER").build()).getId();
    }

    //==중복 방지==//
    private void validateDuplicateMember(UserDto userDto) {

        userRepository.findByEmail(userDto.getEmail())
                .ifPresent( error -> new IllegalStateException("이미 존재하는 회원입니다."));
    }

//    public String login(UserDto userDto){
//
//        UserEntity userEntity = userRepository.findByEmail(userDto.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
//
//        if (!passwordEncoder.matches(userEntity.getPassword(), userDto.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(userName);
        UserEntity userEntity = userEntityWrapper.get();
        // form에서 name = "username"으로 요청해야만 한다...!
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin@example.com").equals(userName)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.toString())); //권한 부여
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.toString())); //권한 부여
        }

        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }
}
