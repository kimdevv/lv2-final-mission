package finalmission.reservation.presentation;

import finalmission.reservation.business.TimeService;
import finalmission.reservation.model.Time;
import finalmission.reservation.presentation.dto.request.TimeCreateRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public Time create(@RequestBody TimeCreateRequest requestBody) {
        return timeService.createTime(requestBody);
    }

    @GetMapping
    public List<Time> findAll() {
        return timeService.findAllTimes();
    }
}
