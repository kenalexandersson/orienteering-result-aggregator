package org.ikuven.resultaggregator;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:result-aggregator.properties")
@ConfigurationProperties(prefix = "ikuven.eventor.client")
public class ResultAggregatorProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultAggregatorProperties.class);

    private String url;

    private String apiKey;

    public String getApiKey() {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equalsIgnoreCase("<get api-key from IK Uven Eventor admin>")) {
            LOGGER.warn("No ApiKey was given in result-aggregator.properties - will not be able to call Eventor API");
        }

        return apiKey;
    }

}

