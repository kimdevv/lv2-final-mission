package finalmission.reservation.presentation;

import finalmission.reservation.business.ReservationService;
import finalmission.reservation.model.Reservation;
import finalmission.reservation.presentation.dto.request.ReservationCreateRequest;
import finalmission.reservation.presentation.dto.response.ReservationGetResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationGetResponse create(@RequestBody ReservationCreateRequest requestBody) {
        Reservation reservation = reservationService.createReservation(requestBody);
        return new ReservationGetResponse(reservation.getId(), reservation.getName(), reservation.getTreatmentType(), reservation.getDate(), reservation.getTime().getStartAt());
    }

    @GetMapping
    public List<ReservationGetResponse> findAll() {
        List<Reservation> reservations = reservationService.findAllReservations();
        return reservations.stream()
                .map(reservation -> new ReservationGetResponse(reservation.getId(), reservation.getName(), reservation.getTreatmentType(), reservation.getDate(), reservation.getTime().getStartAt()))
                .toList();
    }
}
