package de.rjst.rjstintegration;

import de.rjst.rjstintegration.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "json", url = "https://json-api-service.rjst.de/data", configuration = FeignConfig.class)
public interface JsonService {

    @PostMapping
    void sendData(@RequestBody UserEntity userEntity);

}
