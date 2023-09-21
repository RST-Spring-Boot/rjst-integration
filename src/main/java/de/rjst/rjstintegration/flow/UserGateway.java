package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.database.UserEntity;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface UserGateway {

    @Gateway(requestChannel = "insertChannel", replyChannel = "replyChannel")
    UserEntity insertUser(UserEntity user);

}
