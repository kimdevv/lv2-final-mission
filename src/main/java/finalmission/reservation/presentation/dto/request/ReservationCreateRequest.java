package finalmission.reservation.presentation.dto.request;

import finalmission.medical.model.TreatmentType;

import java.time.LocalDate;

public record ReservationCreateRequest(TreatmentType treatmentType, LocalDate date, Long timeId, String name) {
}
