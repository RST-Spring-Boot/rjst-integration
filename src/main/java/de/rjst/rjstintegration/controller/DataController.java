package de.rjst.rjstintegration.controller;


import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import de.rjst.rjstintegration.FlowType;
import de.rjst.rjstintegration.database.UserEntity;
import de.rjst.rjstintegration.database.UserRepository;
import de.rjst.rjstintegration.flow.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
@RequestMapping("data")
public class DataController {

    private final UserGateway userGateway;


    @PostMapping
    public final ResponseEntity<List<UserEntity>> postFakeData(@RequestParam final int amount, @RequestParam final FlowType flowType) {
        final List<UserEntity> result = new ArrayList<>();
        final Faker faker = new Faker(Locale.GERMAN);
        final UserEntity userEntity = new UserEntity();

        for (int i = 1;  i <= amount; i++) {

            final Address address = faker.address();
            userEntity.setId(0L);
            userEntity.setName(address.firstName());
            userEntity.setStatus(0);
            userEntity.setFlowType(flowType);
            result.add(userGateway.insertUser(userEntity));
        }

        return ResponseEntity.ok(result);
    }


}
