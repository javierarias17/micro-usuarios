package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.exception.TechnicalException;
import com.pragma.powerup.domain.exception.constant.TechnicalMessageConstants;
import com.pragma.powerup.domain.spi.IPlazoletaServicePort;
import com.pragma.powerup.infrastructure.out.http.client.IPlazoletaServiceFeignClient;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlazoletaServiceAdapter implements IPlazoletaServicePort {

    private final IPlazoletaServiceFeignClient feignClient;

    @Override
    public boolean isOwnerOfRestaurant(Long restaurantId) {
        try {
            return Boolean.TRUE.equals(feignClient.isOwnerOfRestaurant(restaurantId));
        } catch (RetryableException e) {
            throw new TechnicalException(TechnicalMessageConstants.RESTAURANT_SERVICE_UNAVAILABLE);
        }
    }
}
