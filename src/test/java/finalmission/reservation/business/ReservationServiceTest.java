package finalmission.reservation.business;

import finalmission.holiday.database.HolidayRepository;
import finalmission.holiday.model.Holiday;
import finalmission.medical.model.TreatmentType;
import finalmission.reservation.database.TimeRepository;
import finalmission.reservation.model.Reservation;
import finalmission.reservation.model.Time;
import finalmission.reservation.presentation.dto.request.ReservationCreateRequest;
import finalmission.reservation.presentation.dto.request.ReservationDeleteRequest;
import finalmission.reservation.presentation.dto.request.ReservationUpdateTreatmentTypeRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    TimeRepository timeRepository;

    @Autowired
    HolidayRepository holidayRepository;

    @Test
    void 예약을_생성할_수_있다() {
        // Given
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), "프리");

        // When
        Reservation reservation = reservationService.createReservation(reservationCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservation).isNotNull();
            softAssertions.assertThat(reservation.getTime()).isEqualTo(time);
            softAssertions.assertThat(reservation.getName()).isEqualTo("프리");
        });
    }

    @Test
    void 존재하지_않는_시간_id로는_예약할_수_없다() {
        // Given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now(), 500L, "프리");

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(reservationCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 시간 id입니다.");
    }

    @Test
    void 이미_예약되어_있는_시각에_중복으로_예약할_수_없다() {
        // Given
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        ReservationCreateRequest reservationCreateRequest1 = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), "프리");
        ReservationCreateRequest reservationCreateRequest2 = new ReservationCreateRequest(TreatmentType.SCALING, LocalDate.now().plusDays(1), time.getId(), "프리2");
        reservationService.createReservation(reservationCreateRequest1);

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(reservationCreateRequest2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 시각입니다.");
    }

    @Test
    void 공휴일에는_예약할_수_없다() {
        // Given
        LocalDate nationalLiberationDay = LocalDate.of(2025, 8, 15);
        holidayRepository.save(new Holiday(nationalLiberationDay, "광복절"));
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TreatmentType.EXTRACTION, nationalLiberationDay, time.getId(), "프리");

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(reservationCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("진료하지 않는 날짜입니다.");
    }

    @Test
    void 저장된_모든_예약을_조회할_수_있다() {
        // Given
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        ReservationCreateRequest reservationCreateRequest1 = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), "프리");
        ReservationCreateRequest reservationCreateRequest2 = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(2), time.getId(), "프리");
        Reservation reservation1 = reservationService.createReservation(reservationCreateRequest1);
        Reservation reservation2 = reservationService.createReservation(reservationCreateRequest2);

        // When & Then
        assertThat(reservationService.findAllReservations())
                .containsExactlyInAnyOrder(reservation1, reservation2);
    }

    @Test
    void 주어진_기간_사이의_예약을_모두_조회할_수_있다() {
        // Given
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(2);
        ReservationCreateRequest reservationCreateRequest1 = new ReservationCreateRequest(TreatmentType.EXTRACTION, startDate, time.getId(), "프리");
        ReservationCreateRequest reservationCreateRequest2 = new ReservationCreateRequest(TreatmentType.EXTRACTION, startDate.plusDays(1), time.getId(), "프리");
        ReservationCreateRequest reservationCreateRequest3 = new ReservationCreateRequest(TreatmentType.EXTRACTION, startDate.plusDays(2), time.getId(), "프리");
        ReservationCreateRequest reservationCreateRequest4 = new ReservationCreateRequest(TreatmentType.EXTRACTION, startDate.plusDays(3), time.getId(), "프리");
        Reservation reservation1 = reservationService.createReservation(reservationCreateRequest1);
        Reservation reservation2 = reservationService.createReservation(reservationCreateRequest2);
        Reservation reservation3 = reservationService.createReservation(reservationCreateRequest3);
        reservationService.createReservation(reservationCreateRequest4);

        for (Reservation reservation : reservationService.findReservationOfPeriod(startDate, endDate)) {
            System.out.println(reservation.getName() + " " + reservation.getDate() + " " + reservation.getId());
        }

        // When & Then
        assertThat(reservationService.findReservationOfPeriod(startDate, endDate))
                .containsExactlyInAnyOrder(reservation1, reservation2, reservation3);
    }

    @Test
    void 주어진_멤버로_저장된_예약을_모두_확인할_수_있다() {
        // Given
        String member = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        ReservationCreateRequest reservationCreateRequest1 = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), member);
        ReservationCreateRequest reservationCreateRequest2 = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(2), time.getId(), member);
        ReservationCreateRequest reservationCreateRequest3 = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(3), time.getId(), "다른 사람");
        Reservation reservation1 = reservationService.createReservation(reservationCreateRequest1);
        Reservation reservation2 = reservationService.createReservation(reservationCreateRequest2);
        reservationService.createReservation(reservationCreateRequest3);

        // When & Then
        assertThat(reservationService.findMemberReservations(member))
                .containsExactlyInAnyOrder(reservation1, reservation2);
    }

    @Test
    void 주어진_멤버의_예약을_id로_가져온다() {
        // Given
        String member = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), member);
        Reservation reservation = reservationService.createReservation(reservationCreateRequest);

        // When & Then
        assertThat(reservationService.findDetailedReservationOfMember(reservation.getId(), member)).isEqualTo(reservation);
    }

    @Test
    void 잘못된_예약_id로는_주어진_멤버의_예약을_가져올_수_없다() {
        // Given
        String member = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), member);
        reservationService.createReservation(reservationCreateRequest);

        // When & Then
        assertThatThrownBy(() -> reservationService.findDetailedReservationOfMember(500L, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 id입니다.");
    }

    @Test
    void 주어진_멤버의_예약이_아니라면_예약을_조회할_수_없다() {
        // Given
        String member = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), member);
        Reservation reservation = reservationService.createReservation(reservationCreateRequest);

        // When & Then
        assertThatThrownBy(() -> reservationService.findDetailedReservationOfMember(reservation.getId(), "다른 사람"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 멤버의 예약이 아닙니다.");
    }

    @Test
    void 예약되어_있는_진료의_진료_종류를_바꿀_수_있다() {
        // Given
        String member = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), member));
        ReservationUpdateTreatmentTypeRequest reservationUpdateTreatmentTypeRequest = new ReservationUpdateTreatmentTypeRequest(reservation.getId(), TreatmentType.SCALING, member);

        // When
        Reservation changedReservation = reservationService.changeTreatmentType(reservationUpdateTreatmentTypeRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(changedReservation).isNotNull();
            softAssertions.assertThat(changedReservation.getTreatmentType()).isEqualTo(TreatmentType.SCALING);
            softAssertions.assertThat(changedReservation.getName()).isEqualTo(member);
        });
    }

    @Test
    void 존재하지_않는_예약_id로는_진료_종류를_변경할_수_없다() {
        // Given
        String member = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), member));
        ReservationUpdateTreatmentTypeRequest reservationUpdateTreatmentTypeRequest = new ReservationUpdateTreatmentTypeRequest(500L, TreatmentType.SCALING, member);

        // When & Then
        assertThatThrownBy(() -> reservationService.changeTreatmentType(reservationUpdateTreatmentTypeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 id입니다.");
    }

    @Test
    void 주어진_멤버의_예약이_아니라면_진료_종류를_변경할_수_없다() {
        // Given
        String member = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), member));
        ReservationUpdateTreatmentTypeRequest reservationUpdateTreatmentTypeRequest = new ReservationUpdateTreatmentTypeRequest(reservation.getId(), TreatmentType.SCALING, "다른 사람");

        // When & Then
        assertThatThrownBy(() -> reservationService.changeTreatmentType(reservationUpdateTreatmentTypeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 멤버의 예약이 아닙니다.");
    }
    
    @Test
    void 저장된_예약을_삭제할_수_있다() {
        // Given
        String memeber = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), memeber));
        ReservationDeleteRequest reservationDeleteRequest = new ReservationDeleteRequest(reservation.getId(), memeber);
        int originalCount = reservationService.findAllReservations().size();
        
        // When
        reservationService.deleteById(reservationDeleteRequest);
    
        // Then
        assertThat(reservationService.findAllReservations().size()).isEqualTo(originalCount - 1);
    }
    
    @Test
    void 존재하지_않는_예약_id로는_예약을_삭제할_수_없다() {
        // Given
        String memeber = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        reservationService.createReservation(new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), memeber));
        ReservationDeleteRequest reservationDeleteRequest = new ReservationDeleteRequest(500L, memeber);
        
        // When & Then
        assertThatThrownBy(() -> reservationService.deleteById(reservationDeleteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 id입니다.");
    }
    
    @Test
    void 주어진_멤버의_예약이_아니라면_예약을_삭제할_수_없다() {
        // Given
        String memeber = "프리";
        Time time = timeRepository.save(new Time(LocalTime.now().plusMinutes(1)));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(TreatmentType.EXTRACTION, LocalDate.now().plusDays(1), time.getId(), "다른 사람"));
        ReservationDeleteRequest reservationDeleteRequest = new ReservationDeleteRequest(reservation.getId(), memeber);

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteById(reservationDeleteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 멤버의 예약이 아닙니다.");
    }
}