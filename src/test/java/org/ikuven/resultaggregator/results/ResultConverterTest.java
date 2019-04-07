package org.ikuven.resultaggregator.results;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResultConverterTest {

    private ResultConverter resultConverter = new ResultConverter();

    @Test
    void process() throws IOException, JAXBException {


        List<InternalClassResult> internalClassResults = resultConverter.process("classpath:results.xml", 3);

        internalClassResults.stream()
                .peek(internalClassResult -> System.out.println(internalClassResult.getName()))
                .peek(internalClassResult -> System.out.println(internalClassResult.getNumberOfCompetitors()))
                .flatMap(internalClassResult -> internalClassResult.getInternalPersonResults().stream())
                .forEach(System.out::println);

        assertNotNull(internalClassResults);
    }
}