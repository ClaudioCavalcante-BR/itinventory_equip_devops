package br.com.infnet.itinventory.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.DeserializationFeature;

@Configuration
@ConditionalOnProperty(prefix = "search.es", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class ElasticsearchClientConfig {

    @Value("${search.es.host:localhost}")
    private String host;

    @Value("${search.es.port:9200}")
    private int port;

    @Bean
    public RestClient restClient() {
        //  explicito o "http"
        return RestClient.builder(new HttpHost(host, port, "http")).build();
    }

    // ESTE BEAN resolve com.fasterxml ObjectMapper
    @Bean
    public ObjectMapper elasticsearchObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // injeta o ObjectMapper acima
    @Bean
    public ElasticsearchTransport transport(RestClient restClient, ObjectMapper elasticsearchObjectMapper) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper(elasticsearchObjectMapper));
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }
}