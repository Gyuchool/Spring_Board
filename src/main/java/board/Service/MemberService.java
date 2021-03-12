package board.Service;

import board.Domain.Entity.MemberEntity;
import board.Domain.Repository.MemberRepository;
import board.configuration.JwtTokenProvider;
import board.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    //==Spring Security 회원가입==//
    public Long join(MemberDto memberDto){
        validateDuplicateMember(memberDto);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        memberDto.setPassword(encoder.encode(memberDto.getPassword()));

        return memberRepository.save(memberDto.toEntity()).getId();
    }

    //==중복 방지==//
    private void validateDuplicateMember(MemberDto memberDto) {

        memberRepository.findByEmail(memberDto.getEmail())
                .ifPresent( error -> new IllegalStateException("이미 존재하는 회원입니다."));
    }

    public String login(MemberDto memberDto){

        MemberEntity memberEntity = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if (!passwordEncoder.matches(memberEntity.getPassword(), memberDto.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.generateToken(memberEntity.getUsername());
    }

}
