package org.ikuven.resultaggregator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:result-aggregator.properties")
@ConfigurationProperties(prefix = "ikuven.eventor.client")
public class ResultAggregatorProperties {

    private String url;

    private String apiKey;
}

