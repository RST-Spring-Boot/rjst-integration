package de.rjst.rjstintegration.flow;

import de.rjst.rjstintegration.FlowType;
import de.rjst.rjstintegration.database.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlowAConfigIT {

    @Autowired
    private MessageChannel receiverChannelA;

    @Autowired
    private DirectChannel finishChannel;


    @Container
    static MockServerContainer mockServerContainer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    static MockServerClient mockServerClient;


    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        mockServerClient = new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
        registry.add("spring.cloud.openfeign.client.config.json.url", mockServerContainer::getEndpoint);
    }

    @BeforeEach
    void setupMockServer() {
        mockServerClient.reset();
        mockServerClient.when(HttpRequest.request().withMethod("POST").withPath("/data")).respond(HttpResponse.response().withStatusCode(200).withBody("{\"status\":\"success\"}"));
    }

    @Test
    void testFlowA() throws InterruptedException {
        UserEntity userEntity = new UserEntity();
        userEntity.setFlowType(FlowType.A);

        finishChannel.addInterceptor(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                // Abgefangene Nachricht inspizieren
                System.out.println("Message intercepted: " + message);
                assertThat(message.getPayload()).isInstanceOf(UserEntity.class);
                UserEntity userEntity = (UserEntity) message.getPayload();
                assertThat(userEntity.getFlowType()).isEqualTo(FlowType.A);
                assertThat(userEntity.getStatus()).isEqualTo(12000);
                return message;
            }
        });

        receiverChannelA.send(new GenericMessage<>(userEntity));
    }
}
