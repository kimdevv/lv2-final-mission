package finalmission.member.presentation.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberLoginRequestTest {

    @Test
    void 아이디는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberLoginRequest(null, "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 아이디는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberLoginRequest("   ", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 비밀번호는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberLoginRequest("username", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 비밀번호는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberLoginRequest("username", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 빈 값이 될 수 없습니다.");
    }

}