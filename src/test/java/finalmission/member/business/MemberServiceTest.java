package finalmission.member.business;

import finalmission.general.auth.util.JwtProvider;
import finalmission.member.model.Member;
import finalmission.member.presentation.dto.request.MemberCreateRequest;
import finalmission.member.presentation.dto.request.MemberLoginRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    void 멤버를_생성하여_저장할_수_있다() {
        // Given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("username", "password", "프리");

        // When
        Member member = memberService.createUser(memberCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(member).isNotNull();
            softAssertions.assertThat(member.getUsername()).isEqualTo("username");
            softAssertions.assertThat(member.getPassword()).isEqualTo("password");
            softAssertions.assertThat(member.getName()).isEqualTo("프리");
        });
    }

    @Test
    void 중복된_아이디로는_멤버를_생성할_수_없다() {
        // Given
        MemberCreateRequest memberCreateRequest1 = new MemberCreateRequest("username", "password", "프리");
        MemberCreateRequest memberCreateRequest2 = new MemberCreateRequest("username", "password", "프리2");
        Member member = memberService.createUser(memberCreateRequest1);

        // When & Then
        assertThatThrownBy(() -> memberService.createUser(memberCreateRequest2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }

    @Test
    void 로그인_성공_시_JWT를_반환한다() {
        // Given
        String name = "프리";
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("username", "password", name);
        Member member = memberService.createUser(memberCreateRequest);
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(member.getUsername(), member.getPassword());
        String expected = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLtlITrpqwiLCJleHAiOjE3NTAxNTM3NDV9.bDb6JgQCnJ1t6gfNe1d8iWzwQZ-ukkIL88zrXdc_mvo";
        when(jwtProvider.generateToken(name)).thenReturn(expected);

        // When
        String token = memberService.login(memberLoginRequest);

        // Then
        assertThat(token).isEqualTo(expected);
    }

    @Test
    void 아이디가_틀렸을_경우에는_JWT를_발급해주지_않는다() {
        // Given
        String username = "username";
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(username, "password", "프리");
        Member member = memberService.createUser(memberCreateRequest);
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("invalidUsername", member.getPassword());
        String expected = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLtlITrpqwiLCJleHAiOjE3NTAxNTM3NDV9.bDb6JgQCnJ1t6gfNe1d8iWzwQZ-ukkIL88zrXdc_mvo";
        when(jwtProvider.generateToken(username)).thenReturn(expected);

        // When & Then
        assertThatThrownBy(() -> memberService.login(memberLoginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
    }

    @Test
    void 비밀번호가_틀렸을_경우에는_JWT를_발급해주지_않는다() {
        // Given
        String username = "username";
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(username, "password", "프리");
        Member member = memberService.createUser(memberCreateRequest);
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(member.getUsername(), "invalidPassword");
        String expected = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLtlITrpqwiLCJleHAiOjE3NTAxNTM3NDV9.bDb6JgQCnJ1t6gfNe1d8iWzwQZ-ukkIL88zrXdc_mvo";
        when(jwtProvider.generateToken(username)).thenReturn(expected);

        // When & Then
        assertThatThrownBy(() -> memberService.login(memberLoginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
    }
}