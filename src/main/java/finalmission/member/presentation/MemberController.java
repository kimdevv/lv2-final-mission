package finalmission.member.presentation;

import finalmission.general.util.CookieManager;
import finalmission.member.business.MemberService;
import finalmission.member.model.Member;
import finalmission.member.presentation.dto.request.MemberCreateRequest;
import finalmission.member.presentation.dto.request.MemberLoginRequest;
import finalmission.member.presentation.dto.response.MemberGetResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final CookieManager cookieManager;

    public MemberController(MemberService memberService, CookieManager cookieManager) {
        this.memberService = memberService;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/signUp")
    public ResponseEntity<MemberGetResponse> signUp(@RequestBody MemberCreateRequest requestBody) {
        Member member = memberService.createUser(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberGetResponse(member.getId(), member.getUsername(), member.getName()));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberLoginRequest requestBody, HttpServletResponse response) {
        String accessToken = memberService.login(requestBody);
        ResponseCookie cookie = cookieManager.generateJwtCookie(accessToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
