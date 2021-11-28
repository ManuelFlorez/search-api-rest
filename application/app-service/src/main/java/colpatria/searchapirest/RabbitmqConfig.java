package colpatria.searchapirest;

import colpatria.searchapirest.reactive.events.CustomReactiveMessageSender;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.api.handlers.registered.RegisteredEventListener;
import org.reactivecommons.async.impl.DiscardNotifier;
import org.reactivecommons.async.impl.HandlerResolver;
import org.reactivecommons.async.impl.communications.ReactiveMessageListener;
import org.reactivecommons.async.impl.communications.TopologyCreator;
import org.reactivecommons.async.impl.config.ConnectionFactoryProvider;
import org.reactivecommons.async.impl.config.RabbitProperties;
import org.reactivecommons.async.impl.config.props.AsyncProps;
import org.reactivecommons.async.impl.config.props.BrokerConfigProps;
import org.reactivecommons.async.impl.converters.MessageConverter;
import org.reactivecommons.async.impl.listeners.ApplicationEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;
import reactor.util.retry.Retry;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@Log
@Component
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({
        RabbitProperties.class,
        AsyncProps.class
})
@Import(BrokerConfigProps.class)
public class RabbitmqConfig {

    private static final String SENDER_TYPE = "sender";
    private static final String LISTENER_TYPE = "listener";

    private final AsyncProps asyncProps;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${elk.rabbitmq.host}")
    private String elkHost;

    @Value("${elk.rabbitmq.username}")
    private String elkUserName;

    @Value("${elk.rabbitmq.password}")
    private String elkPassword;

    @Value("${elk.rabbitmq.virtual_host}")
    private String elkVH;

    @Value("${elk.rabbitmq.port}")
    private Integer elkPort;

    @Bean("INTEGRATION")
    @Primary
    public ConnectionFactoryProvider appConnectionFactory(RabbitProperties properties) throws KeyManagementException, NoSuchAlgorithmException {
        final ConnectionFactory factory = new ConnectionFactory();
        PropertyMapper map = PropertyMapper.get();
        map.from(properties::determineHost).whenNonNull().to(factory::setHost);
        map.from(properties::determinePort).to(factory::setPort);
        map.from(properties::determineUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::determinePassword).whenNonNull().to(factory::setPassword);
        map.from(properties::determineVirtualHost).whenNonNull().to(factory::setVirtualHost);
        map.from(properties::getRequestedHeartbeat).whenNonNull().asInt(Duration::getSeconds).to(factory::setRequestedHeartbeat);
        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
        return () -> factory;
    }

    @Bean("ELK")
    public ConnectionFactoryProvider appConnectionFactoryELK(RabbitProperties properties) throws KeyManagementException,
            NoSuchAlgorithmException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(elkHost);
        factory.setUsername(elkUserName);
        factory.setPassword(elkPassword);
        factory.setVirtualHost(elkVH);
        factory.setPort(elkPort);
        factory.setRequestedHeartbeat(1);
        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
        return () -> factory;
    }

    @Bean("senderElk")
    @Primary
    public CustomReactiveMessageSender customReactiveSender(@Qualifier("ELK") ConnectionFactoryProvider provider, MessageConverter converter,
                                                            BrokerConfigProps brokerConfigProps, RabbitProperties rabbitProperties) {
        final Mono<Connection> senderConnection =
                createConnectionMonoElk(provider.getConnectionFactory(), appName, SENDER_TYPE);
        final ChannelPoolOptions channelPoolOptions = new ChannelPoolOptions();
        final PropertyMapper map = PropertyMapper.get();

        map.from(rabbitProperties.getCache().getChannel()::getSize).whenNonNull()
                .to(channelPoolOptions::maxCacheSize);

        final ChannelPool channelPool = ChannelPoolFactory.createChannelPool(
                senderConnection,
                channelPoolOptions
        );

        final Sender sender = RabbitFlux.createSender(new SenderOptions()
                .channelPool(channelPool)
                .resourceManagementChannelMono(channelPool.getChannelMono()
                        .transform(Utils::cache)));

        return new CustomReactiveMessageSender(sender, brokerConfigProps.getAppName(), converter, new TopologyCreator(sender));
    }

    Mono<Connection> createConnectionMonoElk(@Qualifier("ELK") ConnectionFactory factory, String connectionPrefix, String connectionType) {
        return Mono.fromCallable(() -> factory.newConnection(connectionPrefix + " " + connectionType + "elk"))
                .doOnError(err ->
                        log.log(Level.SEVERE, "Error creating connection to RabbitMq Broker. Starting retry process...", err)
                )
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(300))
                        .maxBackoff(Duration.ofMillis(3000)))
                .cache();
    }

    @Primary
    public HandlerResolver integrationResolver(ApplicationContext context) {

        final HandlerRegistry registry = context.getBean("integrationRegistry", HandlerRegistry.class);
        ConcurrentHashMap<String, RegisteredEventListener> eventListeners = registry.getEventListeners()
                .stream()
                .collect(ConcurrentHashMap::new, (map, handler) -> map.put(handler.getPath(), handler),
                        ConcurrentHashMap::putAll);
        return new HandlerResolver(null, eventListeners, null, null);
    }

    @Primary
    public ReactiveMessageListener messageListener(ConnectionFactoryProvider provider) {
        final Mono<Connection> connection =
                createConnectionMono(provider.getConnectionFactory(), appName, LISTENER_TYPE);
        final Receiver receiver = RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connection));
        final Sender sender = RabbitFlux.createSender(new SenderOptions().connectionMono(connection));

        return new ReactiveMessageListener(receiver,
                new TopologyCreator(sender),
                asyncProps.getFlux().getMaxConcurrency(),
                asyncProps.getPrefetchCount());
    }

    Mono<Connection> createConnectionMono(ConnectionFactory factory, String connectionPrefix, String connectionType) {
        return Mono.fromCallable(() -> factory.newConnection(connectionPrefix + " " + connectionType))
                .doOnError(err ->
                        log.log(Level.SEVERE, "Error creating connection to RabbitMq Broker. Starting retry process...", err)
                )
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(300))
                        .maxBackoff(Duration.ofMillis(3000)))
                .cache();
    }

}
