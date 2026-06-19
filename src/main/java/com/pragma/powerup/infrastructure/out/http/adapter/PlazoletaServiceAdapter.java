package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.exception.TechnicalException;
import com.pragma.powerup.domain.exception.constant.TechnicalMessageConstants;
import com.pragma.powerup.domain.spi.IPlazoletaServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
public class PlazoletaServiceAdapter implements IPlazoletaServicePort {

    private static final String ENDPOINT_IS_OWNER = "/api/v1/restaurant/%s/is-owner";

    private final RestTemplate restTemplate;
    private final String restaurantServiceUrl;

    @Override
    public boolean isOwnerOfRestaurant(Long restaurantId) {
        try {
            String url = restaurantServiceUrl + String.format(ENDPOINT_IS_OWNER, restaurantId);
            HttpEntity<Void> request = new HttpEntity<>(buildAuthHeaders());
            Boolean result = restTemplate.exchange(url, HttpMethod.GET, request, Boolean.class).getBody();
            return Boolean.TRUE.equals(result);
        } catch (ResourceAccessException e) {
            throw new TechnicalException(TechnicalMessageConstants.RESTAURANT_SERVICE_UNAVAILABLE);
        }
    }

    @NonNull
    private HttpHeaders buildAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            }
        }
        return headers;
    }
}
