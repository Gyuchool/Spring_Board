package board.Domain.Repository;

import board.Domain.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByEmail(String email);
    Optional<MemberEntity> findByPassword(String password);
}
