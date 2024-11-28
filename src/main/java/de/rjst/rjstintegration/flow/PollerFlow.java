package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.FlowType;
import de.rjst.rjstintegration.adapter.JsonService;
import de.rjst.rjstintegration.database.UserEntity;
import de.rjst.rjstintegration.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class PollerFlow {

    private static final Integer POLLING_INTERVAL = 1;

    private final Supplier<List<UserEntity>> dataSupplier;


    @Bean
    public IntegrationFlow firstFlow() {
        return IntegrationFlow.fromSupplier(dataSupplier,
                        flow -> flow.poller(Pollers.fixedDelay(Duration.ofSeconds(POLLING_INTERVAL))
                        ))
                .split()
                .routeToRecipients(route -> route
                        .<UserEntity> recipient("receiverChannelA", p -> p.getFlowType() == FlowType.A)
                        .<UserEntity> recipient("receiverChannelB", p -> p.getFlowType() == FlowType.B)
                        .<UserEntity> recipient("receiverChannelC", p -> p.getFlowType() == FlowType.C)
                )
                .get();
    }



}
