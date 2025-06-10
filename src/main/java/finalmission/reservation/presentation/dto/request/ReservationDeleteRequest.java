package finalmission.reservation.presentation.dto.request;

public record ReservationDeleteRequest(Long id, String name) {

    public ReservationDeleteRequest {
        if (id == null) {
            throw new IllegalArgumentException("id는 null이 될 수 없습니다.");
        }
        if (name == null) {
            throw new IllegalArgumentException("예약자는 null이 될 수 없습니다.");
        }
    }
}
