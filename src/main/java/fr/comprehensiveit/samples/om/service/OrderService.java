package fr.comprehensiveit.samples.om.service;

import fr.comprehensiveit.samples.om.model.Order;
import fr.comprehensiveit.samples.om.response.OrdersResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.server.EntityResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    private static final String CB_ORDER_CONFIG = "orderConfig";

    @Value("${endpoints.order}")
    private String orderEndpoint;

    private RestTemplate restTemplate = new RestTemplate();

    @TimeLimiter(name = CB_ORDER_CONFIG)
    @Retry(name = CB_ORDER_CONFIG)
    @CircuitBreaker(name = CB_ORDER_CONFIG, fallbackMethod = "getOrderFallback")
    @Bulkhead(name = CB_ORDER_CONFIG)
    public Mono<ResponseEntity<OrdersResponse>> findOrderById(Optional<String> id) {

        OrdersResponse ordersResponse = new OrdersResponse();
        try {
            ordersResponse.setStatus(ordersResponse.SUCCESS);
            String order = getOrderById(id.get());
            log.info(String.format("Order: %s",order));
            ordersResponse.orders = new ArrayList<String>();
            ordersResponse.orders.add(order);
            return Mono.just(ResponseEntity.status(HttpStatus.OK).body(ordersResponse));
        }catch (Exception e) {
            log.error(e.getMessage());
            return Mono.error(e);
        }
    }

    public Mono<ResponseEntity<OrdersResponse>> getOrderFallback(Optional<String> id, NoSuchElementException ex) {
        OrdersResponse ordersResponse = new OrdersResponse();
        ordersResponse.setStatus(ordersResponse.FAILED);
        ordersResponse.errorMessage = "Missing parameter(s) while calling the Order API.";
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ordersResponse));
    }

    public Mono<ResponseEntity<OrdersResponse>> getOrderFallback(Optional<String> id,NullPointerException ex) {
        OrdersResponse ordersResponse = new OrdersResponse();
        ordersResponse.setStatus(ordersResponse.FAILED);
        String url = orderEndpoint + "/get-by-id?orderId=" + id.get();
        ordersResponse.errorMessage = String.format("An error occurred while during the Order API call. Maybe the order does not exists: %s. Url %s.",ex.getMessage(),url);
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ordersResponse));
    }


    public Mono<ResponseEntity<OrdersResponse>> getOrderFallback(Optional<String> id,HttpServerErrorException ex) {
        OrdersResponse ordersResponse = new OrdersResponse();
        ordersResponse.setStatus(ordersResponse.FAILED);
        ordersResponse.errorMessage = String.format("The following HTTP error occurred while during the Order API call: %s",ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ordersResponse));
    }

    public Mono<ResponseEntity<OrdersResponse>> getOrderFallback(Optional<String> id,Exception ex) {
        OrdersResponse ordersResponse = new OrdersResponse();
        ordersResponse.setStatus(ordersResponse.FAILED);
        ordersResponse.errorMessage = "An error occurred while during the Order API call. Please contact your administrator.";
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ordersResponse));
    }

    public Mono<ResponseEntity<OrdersResponse>> getOrderFallback(Optional<String> id) {
        OrdersResponse ordersResponse = new OrdersResponse();
        ordersResponse.setStatus(ordersResponse.FAILED);
        ordersResponse.errorMessage = "No Exception. Please contact your administrator.";
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ordersResponse));
    }

    private String getOrderById(String id) throws Exception,HttpServerErrorException {
        try {
            String url = orderEndpoint + "/get-by-id?orderId=" + id;
            log.info(url);
            return restTemplate.getForObject(url, String.class);
        }catch (HttpServerErrorException e) {
            throw e;
        }catch (Exception e){
            throw e;
        }

    }
}
