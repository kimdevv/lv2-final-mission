package finalmission.reservation.business;

import finalmission.reservation.model.Time;
import finalmission.reservation.presentation.dto.request.TimeCreateRequest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TimeServiceTest {

    @Autowired
    private TimeService timeService;

    @Test
    void 시간을_생성하여_저장한다() {
        // Given
        LocalTime startAt = LocalTime.now().plusMinutes(1);
        TimeCreateRequest timeCreateRequest = new TimeCreateRequest(startAt);

        // When
        Time newTime = timeService.createTime(timeCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(newTime.getId()).isNotNull();
            softAssertions.assertThat(newTime.getStartAt()).isEqualTo(startAt);
        });
    }
}
