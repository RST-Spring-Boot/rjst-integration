package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.adapter.JsonService;
import de.rjst.rjstintegration.database.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class DataSendTransformer implements GenericTransformer<UserEntity, UserEntity> {

    private final JsonService jsonService;

    @Override
    public UserEntity transform(final UserEntity source) {

        try {
            jsonService.sendData(source);
            source.setStatus(20000);
        } catch (Exception exception) {
            source.setStatus(12000);
        }

        return source;
    }
}
