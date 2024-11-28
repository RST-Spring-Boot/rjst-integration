package de.rjst.rjstintegration.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@RequiredArgsConstructor
@Configuration
public class FinishFlowConfig {

    private final MessageHandler dataHandler;

    @Bean
    public MessageChannel finishChannel() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow finishFlow(final MessageChannel finishChannel) {
        return IntegrationFlow.from(finishChannel)
                .handle(dataHandler)
                .get();
    }

}
