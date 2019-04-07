package org.ikuven.resultaggregator.results;

import lombok.Value;
import org.orienteering.datastandard.DateAndOptionalTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Value
public class InternalEvent {

    private String name;

    private ZonedDateTime startDate;

    public static InternalEvent of(String eventName, DateAndOptionalTime startDate) {
        return new InternalEvent(eventName, toZoned(startDate));
    }

    private static ZonedDateTime toZoned(DateAndOptionalTime startDate) {
        ZonedDateTime zoned = null;

        if (startDate != null && startDate.getDate() != null) {
            // This is a fix for Eventor's strange split of date and time into two separate objects, where the date part ends up in wrong timezone (+01:00)
            // Making sure it is instead UTC instead
            zoned = startDate.getDate().toGregorianCalendar().toZonedDateTime().withZoneSameLocal(ZoneId.of("UTC")).plusHours(23);
        }

        return zoned;
    }

    private InternalEvent(String name, ZonedDateTime startDate) {
        this.name = name;
        this.startDate = startDate;
    }
}
