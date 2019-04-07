package org.ikuven.resultaggregator.results;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class InternalResult {

    private InternalEvent event;

    private List<InternalClassResult> results;
}
