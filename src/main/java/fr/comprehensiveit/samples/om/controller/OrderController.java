package fr.comprehensiveit.samples.om.controller;

import fr.comprehensiveit.samples.om.response.OrdersResponse;
import fr.comprehensiveit.samples.om.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.EntityResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/find_order_id")
    public Mono<ResponseEntity<OrdersResponse>> getOrderById (@RequestParam Optional<String> id) {
        //Order order = new Order("ds");
        //return order;

        /*
        Mono<OrdersResponse> orderResponse = orderService.findOrderById(id);
        Map<String,String> mapResult = new HashMap<>();
        orderResponse.subscribe(data -> {
            if ("success".equals(data.getStatus())) {
                mapResult.put("status","success");
            }else{
                mapResult.put("status","failed");
            }
        });

        HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        if("success".equals(mapResult.get("status"))) {
            httpStatus = HttpStatus.OK;
        }
        EntityResponse<Mono<OrdersResponse>> entityResponse = ResponseEntity.status(httpStatus).body(orderResponse,OrdersResponse.class);
        return entityResponse;

         */
        return orderService.findOrderById(id);
    }
}
