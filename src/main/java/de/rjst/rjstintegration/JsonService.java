package de.rjst.rjstintegration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "json", url = "https://json-api-service.rjst.de/data")
public interface JsonService {

    @PostMapping
    void sendData(@RequestBody UserEntity userEntity);

}
