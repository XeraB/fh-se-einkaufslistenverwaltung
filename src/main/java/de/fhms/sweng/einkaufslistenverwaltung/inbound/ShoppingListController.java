package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.*;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/all")
    public List<ShoppingList> getShoppingLists() {
        return this.shoppingListService.getShoppingLists();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a shopping list by its id")
    public ShoppingListDto getShoppingList(@PathVariable Integer id) {
        LOGGER.info("GET-Request of ShoppingList {} received.", id);
        ShoppingList shoppingList = this.shoppingListService.getShoppingList(id);
        return new ShoppingListDto(shoppingList);
    }


    @PostMapping("/{name}")
    public ShoppingListDto addShoppingList(@PathVariable String name) {
        LOGGER.info("POST-Request of ShoppingList {} received.", name);
        ShoppingList shoppingList = this.shoppingListService.addShoppingList(name);
        return new ShoppingListDto(shoppingList);
    }

    @DeleteMapping("/{id}")
    public void deleteShoppingList(@PathVariable Integer id) {
        LOGGER.info("DELETE-Request of ShoppingList {} received.", id);
        this.shoppingListService.deleteShoppingList(id);
    }


    @GetMapping("/{id}/entries/all")
    @Operation(summary = "Get a shopping list by its id")
    public List<ShoppingListProductDto> getAllProductsFromShoppingList(@PathVariable Integer id) {
        List<ShoppingListProduct> shoppingListProduct = this.shoppingListService.getAllProductsFromShoppingList(id);
        List<ShoppingListProductDto> shoppingListProductDtos = new ArrayList<>();
        for (ShoppingListProduct product : shoppingListProduct) {
            shoppingListProductDtos.add(new ShoppingListProductDto(product.getProduct().getId(), product.getShoppingList().getId(), product.getAmount()));
        }
        return shoppingListProductDtos;
    }

    @GetMapping("/{id}/entries/{num}")
    @Operation(summary = "Get an entry from a shopping list")
    public ShoppingListProductDto getProductFromShoppingListById(@PathVariable Integer id, @PathVariable Integer num) {
        LOGGER.info("POST-Request of ShoppingListProduct {} received.", id, num);
        ShoppingListProduct shoppingListProduct = this.shoppingListService.getProductFromShoppingListById(id, num);
        return new ShoppingListProductDto(shoppingListProduct);
    }

    @PostMapping("/{id}/entries")
    @Operation(summary = "Add a product to a shopping list with the product number")
    public ShoppingListProductDto addProductToList(@PathVariable Integer id, @RequestBody ShoppingListProductDto shoppingListProductDto) {
        LOGGER.info("POST-Request of ShoppingListProduct received.");
        ShoppingListProduct shoppingListProduct = this.shoppingListService.addProductToList(id, shoppingListProductDto.getProductId(), shoppingListProductDto.getAmount());
        return new ShoppingListProductDto(shoppingListProduct);
    }

    @PutMapping("/{id}/entries")
    @Operation(summary = "Change the amount of a product on a shopping list")
    public ShoppingListProductDto updateAmount(@PathVariable Integer id, @RequestBody ShoppingListProductDto shoppingListProductDto) {
        LOGGER.info("PUT-Request of ShoppingListProduct received.");
        ShoppingListProduct shoppingListProduct = this.shoppingListService.updateAmount(id, shoppingListProductDto.getProductId(), shoppingListProductDto.getAmount());
        return shoppingListProductDto;
    }

    @DeleteMapping("/{id}/entries/{num}")
    @Operation(summary = "Delete an entry from a shopping list")
    public void deleteProductFromList(@PathVariable Integer id, @PathVariable Integer num) {
        LOGGER.info("DELETE-Request of ShoppingListProduct received.");
        this.shoppingListService.deleteProductFromList(id, num);
    }


    @GetMapping("/products/all")
    @Operation(summary = "Get all products")
    public List<Product> getAllProducts() {
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