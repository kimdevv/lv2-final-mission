package finalmission.reservation.presentation.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationDeleteRequestTest {

    @Test
    void id는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationDeleteRequest(null, "프리"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id는 null이 될 수 없습니다.");
    }

    @Test
    void name은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationDeleteRequest(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자는 null이 될 수 없습니다.");
    }
}
