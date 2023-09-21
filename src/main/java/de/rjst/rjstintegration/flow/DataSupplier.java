package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.FlowType;
import de.rjst.rjstintegration.database.UserEntity;
import de.rjst.rjstintegration.database.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service("dataSupplier")
public class DataSupplier implements Supplier<List<UserEntity>> {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> get() {
        return userRepository.findByStatusAndFlowType(0, FlowType.A);
    }
}
