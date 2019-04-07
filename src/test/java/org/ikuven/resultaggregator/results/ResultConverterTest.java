package org.ikuven.resultaggregator.results;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResultConverterTest {

    private ResultConverter resultConverter = new ResultConverter();

    @Test
    void process() throws IOException, JAXBException {

        FileInputStream xmlContent = new FileInputStream(ResourceUtils.getFile("classpath:results.xml"));
        InternalResult internalResult = resultConverter.process(xmlContent, 3);

        internalResult.getResults().stream()
                .peek(internalClassResult -> System.out.println(internalClassResult.getName()))
                .peek(internalClassResult -> System.out.println(internalClassResult.getNumberOfCompetitors()))
                .flatMap(internalClassResult -> internalClassResult.getInternalPersonResults().stream())
                .forEach(System.out::println);

        assertNotNull(internalResult);
    }

    @Test
    void name() throws IOException, JAXBException {
        FileInputStream xmlContent = new FileInputStream(ResourceUtils.getFile("classpath:results.xml"));
        InternalResult internalResult = resultConverter.process(xmlContent, 3);

        assertThat(internalResult.getEvent().getStartDate())
            .isEqualTo("2019-03-30T23:00:00Z");
    }
}