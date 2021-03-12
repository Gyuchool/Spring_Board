package board.Service;

import board.Domain.Entity.MemberEntity;
import board.Domain.Repository.MemberRepository;
import board.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

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

    public int login(MemberDto memberDto){
        MemberEntity byEmail = memberRepository.findByEmail(memberDto.getEmail()).get();
        MemberEntity byPassword = memberRepository.findByPassword(memberDto.getPassword()).get();

        if(byEmail == byPassword){
            return 1;
        }
        return 0;

    }

    @Override
    public MemberEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
