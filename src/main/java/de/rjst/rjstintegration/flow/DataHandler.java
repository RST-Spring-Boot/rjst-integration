package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.database.UserEntity;
import de.rjst.rjstintegration.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataHandler implements MessageHandler {

    private final UserRepository userRepository;

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        if (message.getPayload() instanceof final UserEntity userEntity) {
            if (userEntity.getStatus() == 20000) {
                userRepository.delete(userEntity);
            } else {
                userRepository.save(userEntity);
                log.error("Error while processing userEntity: {}", userEntity);
            }
        }
    }
}
