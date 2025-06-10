package finalmission.reservation.database;

import finalmission.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT r
        FROM Reservation r
            JOIN FETCH r.time t
        WHERE :startDate <= r.date AND r.date >= :endDate
    """)
    List<Reservation> findReservationOfPeriod(LocalDate startDate, LocalDate endDate);

    Optional<Reservation> findByIdAndName(Long id, String name);

    List<Reservation> findByName(String name);

    boolean existsByDateAndTimeId(LocalDate date, Long timeId);
}
