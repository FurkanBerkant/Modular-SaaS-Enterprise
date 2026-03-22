package pro.turkninja.saas.provider;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record DailySchedule(
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        boolean dayOff
) {}
