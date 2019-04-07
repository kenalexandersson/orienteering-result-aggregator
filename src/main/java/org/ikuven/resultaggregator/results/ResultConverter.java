package org.ikuven.resultaggregator.results;

import org.orienteering.datastandard.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResultConverter {

    public List<InternalClassResult> process(InputStream xmlContent, Integer limit) throws JAXBException {

        return extractResult((ResultList) getUnmarshaller().unmarshal(xmlContent), limit);
    }

    public List<InternalClassResult> process(String fileLocation, Integer limit) throws FileNotFoundException, JAXBException {

        File file = ResourceUtils.getFile(fileLocation);

        return extractResult((ResultList) getUnmarshaller().unmarshal(file), limit);
    }

    private List<InternalClassResult> extractResult(ResultList resultList, Integer limit) {
        return resultList.getClassResult().stream()
                .map((ClassResult classResult) -> toInternalClassResult(classResult, limit))
                .collect(Collectors.toList());
    }

    private InternalClassResult toInternalClassResult(ClassResult classResult, Integer limit) {

        List<InternalPersonResult> internalPersonResults = classResult.getPersonResult().stream()
                .map(personResult -> InternalPersonResult.of(personResult.getPerson().getName().getGiven(), personResult.getPerson().getName().getFamily(), getOrganisationName(personResult), personResult.getResult()))
                .filter(internalPersonResult -> hasPosition(internalPersonResult, limit))
                .collect(Collectors.toList());

        return InternalClassResult.of(classResult.getClazz().getName(), getNumberOfCompetitors(classResult), getStillActive(classResult), getStatus(classResult), internalPersonResults);
    }

    private Long getStillActive(ClassResult classResult) {
        return classResult.getPersonResult().stream()
                .flatMap(personResult -> personResult.getResult().stream())
                .filter(personRaceResult -> personRaceResult.getStatus().equals(ResultStatus.ACTIVE))
                .count();
    }

    private String getStatus(ClassResult classResult) {
        return classResult.getClazz().getRaceClass().stream()
                .reduce((first, second) -> second)
                .map(RaceClass::getStatus)
                .map(RaceClassStatus::value)
                .orElse(null);
    }

    private Integer getNumberOfCompetitors(ClassResult classResult) {
        return classResult.getClazz().getNumberOfCompetitors() != null ? classResult.getClazz().getNumberOfCompetitors().intValue() : 0;
    }

    private boolean hasPosition(InternalPersonResult internalPersonResult, Integer limit) {
        boolean hasPosition;

        if (limit == null) {
            hasPosition = internalPersonResult.getPosition() != null;
        } else {
            hasPosition = internalPersonResult.getPosition() != null && internalPersonResult.getPosition() <= limit;
        }

        return hasPosition;
    }

    private String getOrganisationName(PersonResult personResult) {
        return personResult.getOrganisation() != null ? personResult.getOrganisation().getName() : "";
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ResultList.class);
        return jaxbContext.createUnmarshaller();
    }
}
