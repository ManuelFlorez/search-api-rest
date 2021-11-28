package colpatria.searchapirest.reactive.config;

import colpatria.searchapirest.controller.consultant.ConsultantController;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.impl.config.annotations.EnableMessageListeners;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
@EnableMessageListeners
@RequiredArgsConstructor
public class EventsSubscriptionsConfig {

    private final ConsultantController consultantController;

    @Value("${event.search.api}")
    private String searchApi;

    @Value("${scheduler.timeout}")
    private long timeout;

    @Bean
    public HandlerRegistry eventSubscriptions() {
        return register()
                .listenEvent(searchApi, event -> consultantController.searchApiRest()
                        .timeout(Duration.ofMinutes(timeout)), String.class);
    }

}
