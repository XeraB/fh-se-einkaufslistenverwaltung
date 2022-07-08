package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.security.JwtValidator;
import de.fhms.sweng.einkaufslistenverwaltung.model.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.ProductService;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/rest/shoppingList")
@RequiredArgsConstructor
public class ShoppingListController {

    private ShoppingListService shoppingListService;
    private ProductService productService;
    private JwtValidator jwtValidator;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService, ProductService productService, JwtValidator jwtValidator) {
        this.shoppingListService = shoppingListService;
        this.productService = productService;
        this.jwtValidator = jwtValidator;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get all entries from a shopping list by a user id")
    public Set<ShoppingListProductDto> getAllProductsFromShoppingListByUserId(@RequestHeader String Authorization) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("GET-Request of ShoppingList for User {} received.", userEmail);
        return this.shoppingListService.getAllProductsFromShoppingList(userEmail);
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Add a product to a shopping list with the userId, the product number and amount")
    public Boolean addProductToListByUser(@RequestHeader String Authorization, @RequestBody EntryDto entryDto) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("POST-Request of ShoppingListProduct {} {} {} received.", userEmail, entryDto.getProductId(), entryDto.getAmount());
        return this.shoppingListService.addProductToList(userEmail, entryDto);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Change the amount of a product on a shopping list")
    public ShoppingListProductDto updateAmount(@RequestHeader String Authorization, @RequestBody ShoppingListProductDto shoppingListProductDto) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("PUT-Request of ShoppingListProduct at List {} received.");
        return this.shoppingListService.updateAmount(userEmail, shoppingListProductDto.getProductId(), shoppingListProductDto.getAmount());
    }

    @DeleteMapping("/food")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Delete an entry from a shopping list")
    public void deleteProductFromListAndSendFoodClient(@RequestHeader String Authorization, @RequestBody EntryDto entryDto) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("DELETE-Request and send to FoodService of ShoppingListProduct for User {} at Product {} received.", userEmail, entryDto.getProductId());
        this.shoppingListService.deleteProductFromListAndSendFoodClient(userEmail, entryDto);
    }

    @DeleteMapping("/entry")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Delete an entry from a shopping list")
    public void deleteProductFromList(@RequestHeader String Authorization, @RequestBody EntryDto entryDto) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("DELETE-Request of ShoppingListProduct for User {} at Product {} received.", userEmail, entryDto.getProductId());
        this.shoppingListService.deleteProductFromList(userEmail, entryDto);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Remove user from shopping list and delete the list, when no other user left")
    public void deleteShoppingList(@RequestHeader String Authorization) {
        String userEmail = jwtValidator.getUserEmail(Authorization.substring(7));
        LOGGER.info("DELETE-Request of User {} received.", userEmail);
        this.shoppingListService.deleteShoppingList(userEmail);
    }


    @GetMapping("/products/all")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get all products")
    public Set<Product> getAllProducts() {
        LOGGER.info("GET-Request of all Products received.");
        return this.productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get a product by its id")
    public Product getProductById(@PathVariable Integer id) {
        LOGGER.info("GET-Request of Product {} received.", id);
        return this.productService.getProductById(id);
    }

    @PostMapping("/products/new")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Add a new product")
    public Product addProduct(@RequestBody Product product) {
        LOGGER.info("POST-Request of Product received.");
        return this.productService.addProduct(product);
    }

    @PutMapping("/products/update")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update a product")
    public Product updateProduct(@RequestBody Product product) {
        LOGGER.info("PUT-Request of Product received.");
        return this.productService.updateProduct(product);
    }


    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Delete a product")
    public void deleteProduct(@PathVariable Integer id) {
        LOGGER.info("DELETE-Request of Product {} received.", id);
        this.productService.deleteProduct(id);
    }
}