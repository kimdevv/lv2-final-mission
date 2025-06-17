package finalmission.general.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    JwtProvider jwtProvider = new JwtProvider();

    @Test
    void 토큰을_생성할_수_있다() {
        // Given
        String name = "username";

        // When & Then
        assertThat(jwtProvider.generateToken(name)).startsWith("ey");
    }
}