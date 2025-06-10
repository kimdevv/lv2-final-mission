package finalmission.holiday.business;

import finalmission.holiday.business.dto.response.HolidayGetResponse;
import finalmission.holiday.database.HolidayRepository;
import finalmission.holiday.model.Holiday;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

@Service
public class NationalHolidayService {

    private final HolidayRepository holidayRepository;
    private final NationalHolidayRestClient nationalHolidayRestClient;

    public NationalHolidayService(HolidayRepository holidayRepository, NationalHolidayRestClient nationalHolidayRestClient) {
        this.holidayRepository = holidayRepository;
        this.nationalHolidayRestClient = nationalHolidayRestClient;
    }

    public void createNationalHolidaysOfThisYear() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        int thisYear = Year.now().getValue();
        for (int month=1; month<=12; month++) {
            HolidayGetResponse holidayResponses = nationalHolidayRestClient.getHolidays(thisYear, month);
            for (HolidayGetResponse.Response.Body.Items.HolidayInfo holiday : holidayResponses.getHolidayInfo()) {
                holidayRepository.save(new Holiday(LocalDate.parse(holiday.getLocdate(), dateTimeFormatter), holiday.getDateName()));
            }
        }
    }
}
