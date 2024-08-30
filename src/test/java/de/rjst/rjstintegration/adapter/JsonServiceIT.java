package de.rjst.rjstintegration.adapter;

import de.rjst.rjstintegration.database.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpResponse;
import org.springdoc.core.service.RequestBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockserver.model.HttpRequest.request;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class JsonServiceIT {

    @Autowired
    private JsonService underTest;

    @Container
    static MockServerContainer mockServerContainer =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    static MockServerClient mockServerClient;


    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        mockServerClient = new MockServerClient(
                mockServerContainer.getHost(),
                mockServerContainer.getServerPort()
        );
        registry.add("spring.cloud.openfeign.client.config.json.url", mockServerContainer::getEndpoint);
    }

    @Test
    void sendData() {
        mockServerClient.when(request()
                .withMethod("POST")
                .withPath("/data")).respond(HttpResponse.response().withStatusCode(200));

        underTest.sendData(new UserEntity());
    }

    @Test
    void getData() {
        mockServerClient.when(request()
                .withMethod("GET")
                .withPath("/data/1")).respond(HttpResponse.response().withStatusCode(200).withBody("hello world!"));

        String result = underTest.getData(1L);

        assertThat(result).isEqualTo("hello world!");
    }

    @ParameterizedTest
    @CsvSource({
            "3, hello world!",
            "4, hello universe!",
            "5, hello everyone!"
    })
    void getData2(Long inputId, String expectedResponse) {
        mockServerClient.when(request()
                .withMethod("GET")
                .withPath("/data/" + inputId)).respond(HttpResponse.response().withStatusCode(200).withBody(expectedResponse));

        String result = underTest.getData(inputId);

        assertThat(result).isEqualTo(expectedResponse);
    }

}
