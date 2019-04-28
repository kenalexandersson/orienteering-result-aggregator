package org.ikuven.resultaggregator.web;

import org.ikuven.resultaggregator.results.InternalResult;
import org.ikuven.resultaggregator.results.ResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RestController
public class ResultController {

    @Autowired
    private EventorClient eventorClient;

    @Autowired
    private ResultConverter resultConverter;

    @GetMapping(path = "/results/{eventId}", produces = "application/json")
    public InternalResult getResults(@PathVariable String eventId, @RequestParam(value = "top", required = false) Integer top) throws JAXBException {

        InputStream xmlContent = eventorClient.fetchResults(eventId);

        return resultConverter.process(xmlContent, top);
    }

    @GetMapping(path = "/results/{eventId}/{eventClass}", produces = "application/json")
    public InternalResult getResults(@PathVariable String eventId, @PathVariable String eventClass, @RequestParam(value = "top", required = false) Integer top) throws JAXBException {

        InputStream xmlContent = eventorClient.fetchResults(eventId);

        return resultConverter.process(xmlContent, eventClass, top);
    }

    @GetMapping(path = "/results/dummy", produces = "application/json")
    public InternalResult getDummyResults(@RequestParam(value = "top", required = false) Integer top) throws JAXBException, FileNotFoundException {

        FileInputStream xmlContent = new FileInputStream(ResourceUtils.getFile("classpath:results.xml"));

        return resultConverter.process(xmlContent, top);
    }
}
