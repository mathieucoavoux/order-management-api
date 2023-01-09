
package fr.comprehensiveit.samples.om.service;

import fr.comprehensiveit.samples.om.config.OrderManagementConfig;
import fr.comprehensiveit.samples.om.response.OrdersResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = OrderManagementConfig.class)
@ActiveProfiles("dev")
public class OrderServiceTest {

    @Autowired
    OrderService orderService;
    private static ClientAndServer mockServer;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(8090);
    }



    public void mockOrderGetByIdOK() {
        String body = "{\n" +
                "    \"orderId\": 1,\n" +
                "    \"price\": 200.0,\n" +
                "    \"orderTimestamp\": \"2023-01-09T14:12:13.931+00:00\",\n" +
                "    \"customer\": {\n" +
                "        \"customerId\": 1,\n" +
                "        \"title\": \"Majesty\",\n" +
                "        \"firstname\": \"Soma\",\n" +
                "        \"lastname\": \"Leavyi\",\n" +
                "        \"email\": \"soma.leavyi@email.org\",\n" +
                "        \"address\": \"1 street of Majesty, Cambodia\",\n" +
                "        \"zipCode\": \"2222\"\n" +
                "    }\n" +
                "}";
        new MockServerClient("127.0.0.1",8090)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/v1/order-terminal/get-by-id")
                )
                .respond(
                    response()
                        .withStatusCode(200)
                        .withBody(body)
                );
    }

    private org.apache.http.HttpResponse doGet(String uri) {
        String url = "http://127.0.0.1:8090/"+uri;
        HttpClient client = HttpClientBuilder.create().build();
        org.apache.http.HttpResponse response = null;
        HttpGet get = new HttpGet(url);
        try {
            response=client.execute(get);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    @Test
    public void testFindByIdOk() {
        mockOrderGetByIdOK();
        Mono<ResponseEntity<OrdersResponse>> mreor = orderService.findOrderById(Optional.of("1"));
        OrdersResponse ordersResponse = mreor.block().getBody();
        assert ordersResponse.orders.get(0).contains("soma");
    }
}
