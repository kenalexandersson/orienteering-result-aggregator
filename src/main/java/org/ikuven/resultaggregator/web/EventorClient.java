package org.ikuven.resultaggregator.web;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.ikuven.resultaggregator.ResultAggregatorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Component
public class EventorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventorClient.class);

    @Autowired
    private ResultAggregatorProperties properties;

    public InputStream fetchResults(String eventId) {

        try (CloseableHttpClient httpClient = createAcceptSelfSignedCertificateClient()) {

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            RestTemplate restTemplate = new RestTemplate(requestFactory);

            String resourceUrl = String.format("%s%s", properties.getUrl(), eventId);

            HttpHeaders headers = new HttpHeaders();
            headers.set("ApiKey", properties.getApiKey());

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            LOGGER.info("Calling Eventor API using url {}", resourceUrl);
            ResponseEntity<Resource> result = restTemplate.exchange(resourceUrl, HttpMethod.GET, requestEntity, Resource.class);

            return result != null && result.getBody() != null ? result.getBody().getInputStream() : null;

        } catch (IOException | KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private CloseableHttpClient createAcceptSelfSignedCertificateClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        // use the TrustSelfSignedStrategy to allow Self Signed Certificates
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        // we can optionally disable hostname verification.
        // if you don't want to further weaken the security, you don't have to include this.
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

        // create an SSL Socket Factory to use the SSLContext with the trust self signed certificate strategy
        // and allow all hosts verifier.
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

        // finally create the HttpClient using HttpClient factory methods and assign the ssl socket factory
        return HttpClients
                .custom()
                .setSSLSocketFactory(connectionFactory)
                .build();

    }
}
