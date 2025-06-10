package finalmission.reservation.presentation.dto.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeCreateRequestTest {

    @Test
    void startAt은_null이_될_수_없다() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new TimeCreateRequest(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 시간은 null이 될 수 없습니다.");
    }
}
