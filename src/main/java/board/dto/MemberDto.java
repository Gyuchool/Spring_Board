package board.dto;

import board.Domain.Entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String auth;

    public MemberEntity toEntity(){
        MemberEntity memberEntity = MemberEntity.builder()
                .id(id)
                .email(email)
                .password(password)
                .auth(auth)
                .build();
        return memberEntity;
    }

    public MemberDto(MemberEntity memberEntity){
        this.id = memberEntity.getId();
        this.email = memberEntity.getEmail();
        this.password = memberEntity.getPassword();
        this.auth = memberEntity.getAuth();
    }
}
