package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.*;
import feign.Body;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    public List<ShoppingList> getShoppingLists() {
        return this.shoppingListService.getShoppingLists();
    }

    @GetMapping("/user")
    @Operation(summary = "Get a shopping list by a user id")
    public ShoppingListDto getShoppingListByUserId(@RequestBody Integer userId) {
        LOGGER.info("GET-Request of ShoppingList for user with id {} received.", userId);
        ShoppingList shoppingList = this.shoppingListService.getShoppingListByUserId(userId);
        return new ShoppingListDto(shoppingList);
    }

    @PostMapping("/{name}")
    @Operation(summary = "Add a shopping list for a user with a name")
    public ShoppingListDto addShoppingList(@PathVariable String name, @RequestBody Integer userId) {
        LOGGER.info("POST-Request of ShoppingList {} for user with id {} received.", name, userId);
        ShoppingList shoppingList = this.shoppingListService.addShoppingList(userId, name);
        return new ShoppingListDto(shoppingList);
    }

    @DeleteMapping("/")
    @Operation(summary = "Remove user from shopping list and delete the list, when no other user left")
    public void deleteShoppingList(@RequestBody Integer userId) {
        LOGGER.info("DELETE-Request of ShoppingList {} received.", userId);
        this.shoppingListService.deleteShoppingList(userId);
    }

    @PostMapping("/entries/new")
    @Operation(summary = "Add a product to a shopping list with the userId, the product number and amount")
    public Boolean addProductToListByUser(@RequestBody EntryDto entryDto) {
        LOGGER.info("POST-Request of ShoppingListProduct received.");
        return this.shoppingListService.addProductToList(entryDto);
    }

    @GetMapping("/{id}/entries/all")
    @Operation(summary = "Get the entries from a shopping list by its id")
    public Set<ShoppingListProductDto> getAllProductsFromShoppingList(@PathVariable Integer id) {
        //TODO: Rückgabe des Eintrags mit Produktname, etc.
        Set<ShoppingListProduct> shoppingListProduct = this.shoppingListService.getAllProductsFromShoppingList(id);
        Set<ShoppingListProductDto> shoppingListProductDtos = new HashSet<>();
        for (ShoppingListProduct product : shoppingListProduct) {
            shoppingListProductDtos.add(new ShoppingListProductDto(product.getProduct().getId(), product.getShoppingList().getId(), product.getAmount()));
        }
        return shoppingListProductDtos;
    }

    @PostMapping("/{id}/entries")
    @Operation(summary = "Add a product to a shopping list")
    public ShoppingListProductDto addProductToList(@PathVariable Integer id, @RequestBody ShoppingListProductDto shoppingListProductDto) {
        //TODO: Rückgabe des Eintrags mit Produktname, etc.
        LOGGER.info("POST-Request of ShoppingListProduct at List {} received.", id);
        ShoppingListProduct shoppingListProduct = this.shoppingListService.addProductToList(id, shoppingListProductDto.getProductId(), shoppingListProductDto.getAmount());
        return new ShoppingListProductDto(shoppingListProduct);
    }

    @PutMapping("/{id}/entries")
    @Operation(summary = "Change the amount of a product on a shopping list")
    public ShoppingListProductDto updateAmount(@PathVariable Integer id, @RequestBody ShoppingListProductDto shoppingListProductDto) {
        //TODO: Rückgabe des Eintrags mit Produktname, etc.
        LOGGER.info("PUT-Request of ShoppingListProduct at List {} received.", id);
        ShoppingListProduct shoppingListProduct = this.shoppingListService.updateAmount(id, shoppingListProductDto.getProductId(), shoppingListProductDto.getAmount());
        return shoppingListProductDto;
    }

    @DeleteMapping("/{id}/entries/{num}")
    @Operation(summary = "Delete an entry from a shopping list")
    public void deleteProductFromList(@PathVariable Integer id, @PathVariable Integer num) {
        LOGGER.info("DELETE-Request of ShoppingListProduct at List {} received.", id);
        this.shoppingListService.deleteProductFromList(id, num);
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

    @DeleteMapping("/products/{id}")
    @Operation(summary = "Delete a product")
    public void deleteProduct(@PathVariable Integer id) {
        LOGGER.info("DELETE-Request of Product {} received.", id);
        this.productService.deleteProduct(id);
    }
}