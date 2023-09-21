package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.FlowType;
import de.rjst.rjstintegration.database.UserDto;
import de.rjst.rjstintegration.database.UserEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<UserEntity, UserDto> {
    @Override
    public UserDto apply(UserEntity userEntity) {
        final UserDto result = new UserDto();

        result.setId(userEntity.getId());

        FlowType flowType = userEntity.getFlowType();
        result.setName(userEntity.getName() + flowType.name());
        result.setStatus(userEntity.getStatus());
        result.setFlowType(flowType);

        return result;
    }
}
