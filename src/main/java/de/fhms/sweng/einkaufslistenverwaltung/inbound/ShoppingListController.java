package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.*;
import feign.Body;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/rest/shoppingList")
public class ShoppingListController {

    private ShoppingListService shoppingListService;
    private ProductService productService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService, ProductService productService) {
        this.shoppingListService = shoppingListService;
        this.productService = productService;
    }

    @GetMapping("/")
    public Boolean get() {
        LOGGER.info("GET-Request received.");
        return true;
    }

    @GetMapping("/all/{userId}")
    @Operation(summary = "Get all entries from a shopping list by a user id")
    public Set<ShoppingListProductDto> getAllProductsFromShoppingListByUserId(@PathVariable Integer userId) {
        LOGGER.info("GET-Request of ShoppingList for user {} received.", userId);
        return this.shoppingListService.getAllProductsFromShoppingList(userId);
    }

    @PostMapping("/")
    @Operation(summary = "Add a product to a shopping list with the userId, the product number and amount")
    public Boolean addProductToListByUser(@RequestBody EntryDto entryDto) {
        LOGGER.info("POST-Request of ShoppingListProduct {} {} {} received.", entryDto.getUserId(), entryDto.getProductId(), entryDto.getAmount());
        return this.shoppingListService.addProductToList(entryDto);
    }

    @PutMapping("/")
    @Operation(summary = "Change the amount of a product on a shopping list")
    public ShoppingListProductDto updateAmount(@RequestBody ShoppingListProductDto shoppingListProductDto) {
        //TODO change from shoppinglistid to userid ?
        LOGGER.info("PUT-Request of ShoppingListProduct at List {} received.");
        return this.shoppingListService.updateAmount(shoppingListProductDto.getShoppingListId(), shoppingListProductDto.getProductId(), shoppingListProductDto.getAmount());
    }

    //TODO: Endpoint move Entry from shoppinglist to fridge
    //delete entrie from list
    //send entry to food client

    @DeleteMapping("/entry/{productId}/{userId}")
    @Operation(summary = "Delete an entry from a shopping list")
    public void deleteProductFromList(@PathVariable Integer productId, @PathVariable Integer userId) {
        LOGGER.info("DELETE-Request of ShoppingListProduct for User {} at Product {} received.", userId, productId);
        this.shoppingListService.deleteProductFromList(productId, userId);
    }

    @DeleteMapping("/")
    @Operation(summary = "Remove user from shopping list and delete the list, when no other user left")
    public void deleteShoppingList(@RequestBody Integer userId) {
        LOGGER.info("DELETE-Request of User {} received.", userId);
        this.shoppingListService.deleteShoppingList(userId);
    }


    @GetMapping("/products/all")
    @Operation(summary = "Get all products")
    public Set<Product> getAllProducts() {
        LOGGER.info("GET-Request of all Products received.");
        return this.productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Get a product by its id")
    public Product getProductById(@PathVariable Integer id) {
        LOGGER.info("GET-Request of Product {} received.", id);
        return this.productService.getProductById(id);
    }

    @PostMapping("/products/new")
    @Operation(summary = "Add a new product")
    public Product addProduct(@RequestBody Product product) {
        LOGGER.info("POST-Request of Product received.");
        return this.productService.addProduct(product);
    }

    @PutMapping("/products/update")
    @Operation(summary = "Update a product")
    public Product updateProduct(@RequestBody Product product) {
        LOGGER.info("PUT-Request of Product received.");
        return this.productService.updateProduct(product);
    }


    @DeleteMapping("/products/{id}")
    @Operation(summary = "Delete a product")
    public void deleteProduct(@PathVariable Integer id) {
        LOGGER.info("DELETE-Request of Product {} received.", id);
        this.productService.deleteProduct(id);
    }
}