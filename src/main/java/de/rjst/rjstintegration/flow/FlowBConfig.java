package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.database.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;

@RequiredArgsConstructor
@Configuration
public class FlowBConfig {

    private final GenericTransformer<UserEntity, UserEntity> dataSendTransformer;
    private final MessageChannel finishChannel;

    @Bean
    public MessageChannel receiverChannelB() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow flowB(final MessageChannel receiverChannelB) {
        return IntegrationFlow.from(receiverChannelB)
                .transform(dataSendTransformer)
                .channel(finishChannel)
                .get();
    }

}
