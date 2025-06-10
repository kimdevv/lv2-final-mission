package finalmission.reservation.business;

import finalmission.reservation.database.TimeRepository;
import finalmission.reservation.model.Time;
import finalmission.reservation.presentation.dto.request.TimeCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public Time createTime(TimeCreateRequest timeCreateRequest) {
        return timeRepository.save(new Time(timeCreateRequest.startAt()));
    }

    public List<Time> findAllTimes() {
        return timeRepository.findAll();
    }
}
