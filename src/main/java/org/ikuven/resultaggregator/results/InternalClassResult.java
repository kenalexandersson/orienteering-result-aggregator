package org.ikuven.resultaggregator.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
@ToString
public class InternalClassResult {

    @JsonProperty("class")
    private String name;

    private Integer numberOfCompetitors;

    private Long stillActive;

    private String status;

    @JsonProperty("PersonResult")
    private List<InternalPersonResult> internalPersonResults;

}
