package com.pragma.powerup.infrastructure.out.http.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "micro-plazoleta", url = "${adapter.micro-plazoleta.url}")
public interface IPlazoletaServiceFeignClient {

    @GetMapping("/api/v1/restaurant/{restaurantId}/is-owner")
    Boolean isOwnerOfRestaurant(@PathVariable("restaurantId") Long restaurantId);
}
