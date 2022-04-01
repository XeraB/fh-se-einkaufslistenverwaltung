package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/shoppingList")
public class ShoppingListController {

    private ShoppingListService shoppingListService;
    private ProductService productService;

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
    public ShoppingList getShoppingList(@PathVariable Integer id) {
        return this.shoppingListService.getShoppingList(id);
    }

    @PostMapping("/{name}")
    public ShoppingList addShoppingList(@PathVariable String name) {
        return this.shoppingListService.addShoppingList(name);
    }

    @DeleteMapping("/{id}")
    public void deleteShoppingList(@PathVariable Integer id) {
        this.shoppingListService.deleteShoppingList(id);
    }


    @GetMapping("/{id}/all")
    public List<ShoppingListProduct> getAllProductsFromShoppingList(@PathVariable Integer id) {
        return this.shoppingListService.getAllProductsFromShoppingList(id);
    }

    @GetMapping("/{id}/{num}")
    public ShoppingListProduct getProductFromShoppingListById(@PathVariable Integer id, @PathVariable Integer num) {
        return this.shoppingListService.getProductFromShoppingListById(id, num);
    }

    @PostMapping("/{id}/{num}/{amount}")
    public ShoppingListProduct addProductToList(@PathVariable Integer id, @PathVariable Integer num, @PathVariable Integer amount) {
        return this.shoppingListService.addProductToList(id, num, amount);
    }

    @PutMapping("/{id}/{num}/{amount}")
    public ShoppingListProduct updateAmount(@PathVariable Integer id, @PathVariable Integer num, @PathVariable Integer amount) {
        return this.shoppingListService.updateAmount(id, num, amount);
    }

    @DeleteMapping("/{id}/{num}")
    public void deleteProductFromList(@PathVariable Integer id, @PathVariable Integer num) {
        this.shoppingListService.deleteProductFromList(id, num);
    }


    @GetMapping("/products/all")
    public List<Product> getAllProducts() {
        return this.productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return this.productService.getProductById(id);
    }

    @PostMapping("/products/add")
    public Product addProduct(@RequestBody Product product) {
        return this.productService.addProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        this.productService.deleteProduct(id);
    }
}