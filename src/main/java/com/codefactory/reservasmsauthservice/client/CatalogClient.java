package com.codefactory.reservasmsauthservice.client;

import com.codefactory.reservasmsauthservice.dto.response.CategoryResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "catalog-service", url = "${services.catalog.url:http://localhost:8082}")
public interface CatalogClient {

    @GetMapping("/api/catalog/categories/{id}")
    CategoryResponseDTO getCategoryById(@PathVariable UUID id);
}
