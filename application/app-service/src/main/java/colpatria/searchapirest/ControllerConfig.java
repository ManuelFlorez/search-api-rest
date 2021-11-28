package colpatria.searchapirest;

import colpatria.searchapirest.controller.consultant.ConsultantController;
import colpatria.searchapirest.domain.common.CustomEventsGateway;
import colpatria.searchapirest.domain.common.EventsGateway;
import colpatria.searchapirest.domain.messagedata.gateway.CommunicationApi;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class ControllerConfig {

    @Bean
    public WebClient webClient() {
        String connectionProviderName = "customConnectionProvider";
        int acquireTimeout = 20;
        ConnectionProvider connectionProvider = ConnectionProvider.builder(connectionProviderName)
                .maxConnections(50)
                .pendingAcquireTimeout(Duration.ofSeconds(acquireTimeout))
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .tcpConfiguration(tcpClient -> tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 8000)
                        .doOnConnected(connection -> {
                            connection.addHandlerLast(new ReadTimeoutHandler(20));
                            connection.addHandlerLast(new WriteTimeoutHandler(20));
                        }));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ConsultantController consultantController(CustomEventsGateway customEventsGateway,
                                                     EventsGateway eventsGateway, CommunicationApi communicationApi) {
        return new ConsultantController(customEventsGateway, eventsGateway, communicationApi);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }

}
