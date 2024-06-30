package de.rjst.rjstintegration.adapter;

import de.rjst.rjstintegration.database.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "json", configuration = FeignConfig.class)
public interface JsonService {

    @PostMapping("data")
    void sendData(@RequestBody UserEntity userEntity);

}
