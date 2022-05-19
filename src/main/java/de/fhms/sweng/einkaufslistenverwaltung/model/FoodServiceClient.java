package de.fhms.sweng.einkaufslistenverwaltung.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "${food.service.url}", name = "FoodService")
public interface FoodServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/{productId}/{amount}")
    public FoodEntry postFoodEntry(@PathVariable("userId") Integer userId, @PathVariable("productId") Integer productId, @PathVariable("amount") Integer amount);
}
