package finalmission.holiday.presentation;

import finalmission.holiday.business.HolidayService;
import finalmission.holiday.model.Holiday;
import finalmission.holiday.presentation.dto.request.HolidayCreateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/holidays")
@RestController
public class HolidayController {

    private HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping
    public Holiday create(@RequestBody HolidayCreateRequest requestBody) {
        return holidayService.create(requestBody);
    }

    @PostMapping("/national")
    public void createNationalHolidaysOfThisYear() {
        holidayService.createNationalHolidaysOfThisYear();
    }
}
