package de.fhms.sweng.einkaufslistenverwaltung.model;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.types.EntryDto;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.FoodEntry;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "${food.service.url}", name = "FoodService")
public interface FoodServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/")
    FoodEntry postFoodEntry(@RequestBody EntryDto entryDto);
}
