package finalmission.member.presentation;

import finalmission.member.business.MemberService;
import finalmission.member.model.Member;
import finalmission.member.presentation.dto.request.MemberCreateRequest;
import finalmission.member.presentation.dto.response.MemberGetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<MemberGetResponse> signUp(@RequestBody MemberCreateRequest requestBody) {
        Member member = memberService.createUser(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberGetResponse(member.getId(), member.getUsername(), member.getName()));
    }
}
