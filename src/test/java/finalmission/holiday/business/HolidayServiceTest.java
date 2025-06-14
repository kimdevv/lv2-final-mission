package finalmission.holiday.business;

import finalmission.holiday.model.Holiday;
import finalmission.holiday.presentation.dto.request.HolidayCreateRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @Test
    @Transactional
    void 휴일을_생성하여_저장한다() {
        // Given
        LocalDate date = LocalDate.now().plusDays(1);
        String name = "테스트 휴일";
        HolidayCreateRequest holidayCreateRequest = new HolidayCreateRequest(date, name);

        // When
        Holiday newHoliday = holidayService.create(holidayCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(newHoliday.getId()).isNotNull();
            softAssertions.assertThat(newHoliday.getDate()).isEqualTo(date);
            softAssertions.assertThat(newHoliday.getName()).isEqualTo(name);
        });
    }
}