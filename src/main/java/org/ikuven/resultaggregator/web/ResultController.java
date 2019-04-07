package org.ikuven.resultaggregator.web;

import org.ikuven.resultaggregator.results.InternalClassResult;
import org.ikuven.resultaggregator.results.ResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class ResultController {

    @Autowired
    private EventorClient eventorClient;

    @Autowired
    private ResultConverter resultConverter;

    @GetMapping(path = "/results/{eventId}", produces = "application/json")
    public List<InternalClassResult> getResults(@PathVariable String eventId, @RequestParam(value = "limit", required = false) Integer limit) throws IOException, JAXBException {

        InputStream xmlContent = eventorClient.fetchResults(eventId);

        return resultConverter.process(xmlContent, limit);
    }
}
