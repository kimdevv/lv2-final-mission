package finalmission.member.business;

import finalmission.member.database.MemberRepository;
import finalmission.member.model.Member;
import finalmission.member.presentation.dto.request.MemberCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createUser(MemberCreateRequest memberCreateRequest) {
        String username = memberCreateRequest.username();
        validateDuplicatedUsername(username);
        return memberRepository.save(new Member(username, memberCreateRequest.password(), memberCreateRequest.name()));
    }

    private void validateDuplicatedUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
    }
}
