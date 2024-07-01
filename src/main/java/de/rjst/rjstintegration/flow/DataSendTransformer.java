package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.adapter.JsonService;
import de.rjst.rjstintegration.database.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataSendTransformer implements GenericTransformer<UserEntity, UserEntity> {

    private final JsonService jsonService;

    @Override
    public UserEntity transform(final UserEntity source) {

        try {
            log.info("Sending data: {}", source);
            jsonService.sendData(source);
            source.setStatus(20000);
        } catch (Exception ex) {
            source.setStatus(12000);
            log.error("Error while sending data: {}", source, ex);
        }

        return source;
    }
}
