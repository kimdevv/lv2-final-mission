package finalmission.member.presentation.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberCreateRequestTest {

    @Test
    void 아이디는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest(null, "password", "name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 아이디는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest("   ", "password", "name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 비밀번호는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest("username", null, "name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 비밀번호는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest("username", "   ", "name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 이름은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest("username", "password", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈 값이 될 수 없습니다.");
    }

    @Test
    void 이름은_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest("username", "password", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈 값이 될 수 없습니다.");
    }
}