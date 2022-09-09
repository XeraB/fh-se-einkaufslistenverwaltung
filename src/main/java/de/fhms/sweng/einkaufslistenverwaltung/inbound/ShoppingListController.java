package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.security.JwtValidator;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.types.EntryDto;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ShoppingListController implements ShoppingListControllerApi {

    private ShoppingListService shoppingListService;
    private JwtValidator jwtValidator;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService, JwtValidator jwtValidator) {
        this.shoppingListService = shoppingListService;
        this.jwtValidator = jwtValidator;
    }
    
    public void addUserToNewOrExistingList(@RequestHeader String Authorization, @RequestBody(required = false) String inviteCode) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        if (inviteCode != null) {
            LOGGER.info("POST-Request for User {} with InviteCode {} received.", userEmail, inviteCode);
            this.shoppingListService.addUserToShoppingList(userEmail, inviteCode);
        } else {
            LOGGER.info("POST-Request for User {} received.", userEmail);
            this.shoppingListService.addUserWithNewShoppingList(userEmail);
        }
    }

    public Set<ShoppingListProductDto> getAllProductsFromShoppingListByUserId(@RequestHeader String Authorization) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("GET-Request of ShoppingList for User {} received.", userEmail);
        return this.shoppingListService.getAllProductsFromShoppingList(userEmail);
    }

    public ShoppingListProductDto addProductToListByUser(@RequestHeader String Authorization, @RequestBody EntryDto entryDto) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("POST-Request of ShoppingListProduct for User {} and Product {} and Amount {} received.", userEmail, entryDto.getProductId(), entryDto.getAmount());
        return this.shoppingListService.addProductToList(userEmail, entryDto);
    }

    public ShoppingListProductDto updateAmount(@RequestHeader String Authorization, @RequestBody ShoppingListProductDto shoppingListProductDto) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("PUT-Request of ShoppingListProduct at List {} received.");
        return this.shoppingListService.updateAmount(userEmail, shoppingListProductDto.getProductId(), shoppingListProductDto.getAmount());
    }

    public void deleteProductFromListAndSendFoodClient(@RequestHeader String Authorization, @RequestBody EntryDto entryDto) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("DELETE-Request and send to FoodService of ShoppingListProduct for User {} and Product {} received.", userEmail, entryDto.getProductId());
        this.shoppingListService.deleteProductFromListAndSendFoodClient(userEmail, entryDto);
    }

    public void deleteProductFromList(@RequestHeader String Authorization, @PathVariable Integer productId) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("DELETE-Request of ShoppingListProduct for User {} and Product {} received.", userEmail, productId);
        this.shoppingListService.deleteProductFromList(userEmail, productId);
    }

    public void deleteShoppingList(@RequestHeader String Authorization) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("DELETE-Request of User {} received.", userEmail);
        this.shoppingListService.deleteShoppingList(userEmail);
    }
}