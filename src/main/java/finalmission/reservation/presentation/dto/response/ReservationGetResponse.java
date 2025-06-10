package finalmission.reservation.presentation.dto.response;

import finalmission.medical.model.TreatmentType;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationGetResponse(Long id, TreatmentType treatmentType, LocalDate date, LocalTime time) {
}
