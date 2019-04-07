package org.ikuven.resultaggregator.results;

import lombok.ToString;
import lombok.Value;
import org.orienteering.datastandard.PersonRaceResult;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Value
@ToString
public class InternalPersonResult {

    private String firstName;
    private String lastName;
    private String organisationName;
    private ZonedDateTime startTime;
    private ZonedDateTime finishTime;
    private Duration duration;
    private Integer position;
    private InternalStatus status;

    public static InternalPersonResult of(String firstName, String lastName, String organisationName, List<PersonRaceResult> resultList) {

        PersonRaceResult lastResult = getLastResult(resultList);
        ZonedDateTime startTime = toZoned(lastResult.getStartTime());
        ZonedDateTime finishTime = toZoned(lastResult.getFinishTime());
        Integer position = toPosition(lastResult.getPosition());
        InternalStatus status = InternalStatus.fromValue(lastResult.getStatus().value());

        return new InternalPersonResult(firstName, lastName, organisationName, startTime, finishTime, position, status);
    }

    private InternalPersonResult(String firstName, String lastName, String organisationName, ZonedDateTime startTime, ZonedDateTime finishTime, Integer position, InternalStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisationName = organisationName;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.duration = toDuration(startTime, finishTime);
        this.position = position;
        this.status = status;
    }

    private static Duration toDuration(ZonedDateTime startTime, ZonedDateTime finishTime) {
        return startTime != null && finishTime != null ? Duration.between(startTime, finishTime) : Duration.ZERO;
    }

    private static PersonRaceResult getLastResult(List<PersonRaceResult> resultList) {
        return resultList.stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    private static Integer toPosition(BigInteger position) {
        return position != null ? position.intValue() : null;
    }

    private static ZonedDateTime toZoned(XMLGregorianCalendar time) {
        return time != null ? time.toGregorianCalendar().toZonedDateTime() : null;
    }
}
