package finalmission.reservation.database;

import finalmission.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByIdAndName(Long id, String name);

    List<Reservation> findByName(String name);

    boolean existsByDateAndTimeId(LocalDate date, Long timeId);
}
