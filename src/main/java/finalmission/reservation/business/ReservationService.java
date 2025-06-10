package finalmission.reservation.business;

import finalmission.holiday.business.HolidayService;
import finalmission.reservation.database.ReservationRepository;
import finalmission.reservation.database.TimeRepository;
import finalmission.reservation.model.Reservation;
import finalmission.reservation.model.Time;
import finalmission.reservation.presentation.dto.request.ReservationCreateRequest;
import finalmission.reservation.presentation.dto.request.ReservationDeleteRequest;
import finalmission.reservation.presentation.dto.request.ReservationUpdateTreatmentTypeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Reservation changeTreatmentType(ReservationUpdateTreatmentTypeRequest reservationUpdateTreatmentTypeRequest) {
        Reservation reservation = reservationRepository.findById(reservationUpdateTreatmentTypeRequest.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 id입니다."));
        validateSameMember(reservation.getName(), reservationUpdateTreatmentTypeRequest.name());
        reservationRepository.delete(reservation);
        return reservationRepository.save(new Reservation(reservationUpdateTreatmentTypeRequest.treatmentType(), reservation.getDate(), reservation.getTime(), reservation.getName()));
    }

    private void validateSameMember(String savedName, String requestedName) {
        if (savedName.equalsIgnoreCase(requestedName)) {
            return;
        }
        throw new IllegalArgumentException("예약을 등록한 유저만 수정할 수 있습니다.");
    }

    public void deleteById(ReservationDeleteRequest reservationDeleteRequest) {
        Reservation reservation = reservationRepository.findById(reservationDeleteRequest.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 id입니다."));
        validateSameMember(reservation.getName(), reservationDeleteRequest.name());
        reservationRepository.delete(reservation);
    }
}
