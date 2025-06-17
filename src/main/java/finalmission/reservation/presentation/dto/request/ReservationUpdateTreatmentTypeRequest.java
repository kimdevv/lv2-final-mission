package finalmission.reservation.presentation.dto.request;

import finalmission.medical.model.TreatmentType;

public record ReservationUpdateTreatmentTypeRequest(Long id, TreatmentType treatmentType, String name) {

    public ReservationUpdateTreatmentTypeRequest {
        if (id == null) {
            throw new IllegalArgumentException("id는 null이 될 수 없습니다.");
        }
        if (treatmentType == null) {
            throw new IllegalArgumentException("진료 종류는 null이 될 수 없습니다.");
        }
        if (name == null) {
            throw new IllegalArgumentException("예약자는 null이 될 수 없습니다.");
        }
    }
}
