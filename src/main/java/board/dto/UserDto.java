package board.dto;

import board.Domain.Entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String auth;


    public UserDto(UserEntity userEntity){

        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.auth = userEntity.getAuth();
    }
}
