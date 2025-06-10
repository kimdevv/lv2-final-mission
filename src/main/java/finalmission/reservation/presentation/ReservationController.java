package finalmission.reservation.presentation;

import finalmission.reservation.business.ReservationService;
import finalmission.reservation.model.Reservation;
import finalmission.reservation.presentation.dto.request.ReservationCreateRequest;
import finalmission.reservation.presentation.dto.request.ReservationDeleteRequest;
import finalmission.reservation.presentation.dto.request.ReservationUpdateTreatmentTypeRequest;
import finalmission.reservation.presentation.dto.response.ReservationGetDetailResponse;
import finalmission.reservation.presentation.dto.response.ReservationGetResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationGetDetailResponse create(@RequestBody ReservationCreateRequest requestBody) {
        Reservation reservation = reservationService.createReservation(requestBody);
        return new ReservationGetDetailResponse(reservation.getId(), reservation.getName(), reservation.getTreatmentType(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getCreatedAt());
    }

    @GetMapping
    public List<ReservationGetResponse> findAll() {
        List<Reservation> reservations = reservationService.findAllReservations();
        return reservations.stream()
                .map(reservation -> new ReservationGetResponse(reservation.getId(), reservation.getTreatmentType(), reservation.getDate(), reservation.getTime().getStartAt()))
                .toList();
    }

    @GetMapping
    public List<ReservationGetResponse> findReservationsOfPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Reservation> reservations = reservationService.findReservationOfPeriod(startDate, endDate);
        return reservations.stream()
                .map(reservation -> new ReservationGetResponse(reservation.getId(), reservation.getTreatmentType(), reservation.getDate(), reservation.getTime().getStartAt()))
                .toList();
    }

    @GetMapping("/{name}")
    public List<ReservationGetResponse> findMemberReservations(@PathVariable String name) {
        List<Reservation> reservations = reservationService.findMemberReservations(name);
        return reservations.stream()
                .map(reservation -> new ReservationGetResponse(reservation.getId(), reservation.getTreatmentType(), reservation.getDate(), reservation.getTime().getStartAt()))
                .toList();
    }

    @GetMapping("/{name}/{id}")
    public ReservationGetDetailResponse findMyReservationDetail(@PathVariable String name, @PathVariable Long id) {
        Reservation reservation = reservationService.findDetailedReservationOfMember(id, name);
        return new ReservationGetDetailResponse(reservation.getId(), reservation.getName(), reservation.getTreatmentType(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getCreatedAt());
    }

    @PatchMapping
    public ReservationGetResponse update(@RequestBody ReservationUpdateTreatmentTypeRequest requestBody) {
        Reservation reservation = reservationService.changeTreatmentType(requestBody);
        return new ReservationGetResponse(reservation.getId(), reservation.getTreatmentType(), reservation.getDate(), reservation.getTime().getStartAt());
    }

   @DeleteMapping
    public void delete(@RequestBody ReservationDeleteRequest requestBody) {
       reservationService.deleteById(requestBody);
   }
}
