package finalmission.reservation.business;

import finalmission.holiday.business.HolidayService;
import finalmission.reservation.database.ReservationRepository;
import finalmission.reservation.database.TimeRepository;
import finalmission.reservation.model.Reservation;
import finalmission.reservation.model.Time;
import finalmission.reservation.presentation.dto.request.ReservationCreateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final HolidayService holidayService;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, HolidayService holidayService) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.holidayService = holidayService;
    }

    public Reservation createReservation(ReservationCreateRequest reservationCreateRequest) {
        LocalDate date = reservationCreateRequest.date();
        Long timeId = reservationCreateRequest.timeId();
        validateDuplicatedReservation(date, timeId);
        validateHoliday(date);
        Time time = timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간 id입니다."));
        return reservationRepository.save(new Reservation(reservationCreateRequest.treatmentType(), date, time, reservationCreateRequest.name()));
    }

    private void validateDuplicatedReservation(LocalDate date, Long timeId) {
        if (reservationRepository.existsByDateAndTimeId(date, timeId)) {
            throw new IllegalArgumentException("이미 예약된 시각입니다.");
        }
    }

    private void validateHoliday(LocalDate date) {
        if (holidayService.existsByDate(date)) {
            throw new IllegalArgumentException("진료하지 않는 날짜입니다.");
        }
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
}
