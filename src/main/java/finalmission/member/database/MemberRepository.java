package finalmission.member.database;

import finalmission.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);
}
