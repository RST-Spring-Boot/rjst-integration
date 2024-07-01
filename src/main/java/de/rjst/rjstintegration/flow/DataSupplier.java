package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.FlowType;
import de.rjst.rjstintegration.database.UserEntity;
import de.rjst.rjstintegration.database.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service("dataSupplier")
public class DataSupplier implements Supplier<List<UserEntity>> {

    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<UserEntity> get() {
        final List<UserEntity> users = userRepository.findByStatus(0);
        users.forEach(user -> user.setStatus(10000));
        return userRepository.saveAll(users);
    }
}
