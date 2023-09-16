package de.rjst.rjstintegration;


import com.github.javafaker.Address;
import com.github.javafaker.Faker;
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

    private final UserRepository userRepository;


    @PostMapping
    public final ResponseEntity<List<UserEntity>> postFakeData(@RequestParam final int amount) {
        final List<UserEntity> result = new ArrayList<>();
        final Faker faker = new Faker(Locale.GERMAN);
        final UserEntity userEntity = new UserEntity();

        for (int i = 0;  i <= amount; i++) {

            final Address address = faker.address();
            userEntity.setName(address.firstName());
            userEntity.setId(0L);
            result.add(userRepository.save(userEntity));
        }

        return ResponseEntity.ok(result);
    }


}
