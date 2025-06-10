package finalmission.reservation.presentation.dto.request;

import finalmission.medical.model.TreatmentType;

public record ReservationUpdateTreatmentTypeRequest(Long id, TreatmentType treatmentType) {
}
