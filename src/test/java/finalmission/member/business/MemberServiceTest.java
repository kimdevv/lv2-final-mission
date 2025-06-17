package finalmission.member.business;

import finalmission.member.model.Member;
import finalmission.member.presentation.dto.request.MemberCreateRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

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
}