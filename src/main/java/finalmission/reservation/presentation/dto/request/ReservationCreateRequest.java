package finalmission.reservation.presentation.dto.request;

import finalmission.medical.model.TreatmentType;

import java.time.LocalDate;

public record ReservationCreateRequest(TreatmentType treatmentType, LocalDate date, Long timeId, String name) {

    public ReservationCreateRequest {
        if (treatmentType == null) {
            throw new IllegalArgumentException("진료 종류는 null이 될 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 null이 될 수 없습니다.");
        }
        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간은 null이 될 수 없습니다.");
        }
        if (name == null) {
            throw new IllegalArgumentException("예약자는 null이 될 수 없습니다.");
        }
    }
}
