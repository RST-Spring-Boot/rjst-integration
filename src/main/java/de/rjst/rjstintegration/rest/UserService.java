package de.rjst.rjstintegration.rest;

import de.rjst.rjstintegration.database.UserEntity;
import de.rjst.rjstintegration.database.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class UserService {

    private final UserRepository userRepository;

    @ServiceActivator(inputChannel = "insertChannel")
    public void insertUser(final Message<UserEntity> message) {
        final UserEntity payload = message.getPayload();
        final UserEntity userEntity = userRepository.saveAndFlush(payload);
        final MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
        replyChannel.send(
                MessageBuilder.withPayload(userEntity)
                .build());
    }

}
