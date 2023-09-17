package de.rjst.rjstintegration;

import de.rjst.rjstintegration.adapter.JsonService;
import de.rjst.rjstintegration.database.UserEntity;
import de.rjst.rjstintegration.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendDataFlow {

    private static final String URL = "https://json-api-service.rjst.de/data";

    private final UserRepository userRepository;
    private final JsonService jsonService;


    @InboundChannelAdapter(channel = "databaseToInterfaceChannel", poller = @Poller(fixedDelay = "1000"))
    public List<UserEntity> fetchDataFromDatabase() {
        return userRepository.findByStatusAndFlowType(0, FlowType.A);
    }

    @ServiceActivator(inputChannel = "databaseToInterfaceChannel")
    public void sendDataToInterface(List<UserEntity> users) {
        for (final UserEntity user : users) {
            jsonService.sendData(user);
            user.setStatus(1);
            userRepository.saveAndFlush(user);
        }
    }

    @Bean
    public MessageChannel databaseToInterfaceChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }


}
