package de.rjst.rjstintegration;

import de.rjst.rjstintegration.adapter.JsonService;
import de.rjst.rjstintegration.database.UserEntity;
import de.rjst.rjstintegration.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendDataFlow {

    private static final String URL = "https://json-api-service.rjst.de/data";

    private final UserRepository userRepository;
    private final JsonService jsonService;


    @ServiceActivator(inputChannel = "receiverChannel")
    public void sendDataToInterface(List<UserEntity> users) {
        for (final UserEntity user : users) {
            jsonService.sendData(user);
            user.setStatus(1);
            userRepository.saveAndFlush(user);
        }
    }

    @Bean
    public MessageChannel insertChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel receiverChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel replyChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow firstFlow(@Qualifier("dataSupplier") final Supplier<List<UserEntity>> dataSupplier,
                                     final MessageChannel receiverChannel) {
        return IntegrationFlow.fromSupplier(dataSupplier,
                        flow -> flow.poller(Pollers.fixedDelay(Duration.ofSeconds(10L))))
                .channel(receiverChannel)
                .get();
    }


}
