package finalmission.member.database;

import finalmission.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
    boolean existsByUsername(String username);
}
