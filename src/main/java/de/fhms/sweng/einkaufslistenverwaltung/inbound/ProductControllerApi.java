package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/rest/shoppingList/products")
public interface ProductControllerApi {

    String userRole = "hasAnyAuthority('USER','PREMIUM','ADMIN')";
    String premiumRole = "hasAnyAuthority('PREMIUM','ADMIN')";
    String adminRole = "hasAnyAuthority('ADMIN')";

    @GetMapping("/all")
    @PreAuthorize(userRole)
    @Operation(summary = "Get all products",
            responses = {
                    @ApiResponse(description = "All Products",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "No Product found",
                            responseCode = "404"
                    )
            })
    Set<Product> getAllProducts();

    @GetMapping("/{id}")
    @PreAuthorize(userRole)
    @Operation(summary = "Get a product by its id",
            responses = {
                    @ApiResponse(description = "The Product with the requested id",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Requested Product doesn't exist",
                            responseCode = "404"
                    )
            })
    Product getProductById(@PathVariable Integer id);

    @PostMapping("/new")
    @PreAuthorize(premiumRole)
    @Operation(summary = "Add a new product",
            responses = {
                    @ApiResponse(description = "The new Product",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Product already exists",
                            responseCode = "409"
                    ),
                    @ApiResponse(description = "Error while adding Product",
                            responseCode = "404"
                    ),
                    @ApiResponse(description = "Error while publishing Product",
                            responseCode = "404"
                    )
            })
    Product addProduct(@RequestBody Product product);

    @PutMapping("/update")
    @PreAuthorize(adminRole)
    @Operation(summary = "Update a product",
            responses = {
                    @ApiResponse(description = "The updated Product",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Product doesn't exist. Please add Product first.",
                            responseCode = "404"
                    ),
                    @ApiResponse(description = "Error while publishing Product",
                            responseCode = "404"
                    )
            })
    Product updateProduct(@RequestBody Product product);


    @DeleteMapping("/{id}")
    @PreAuthorize(adminRole)
    @Operation(summary = "Delete a product",
            responses = {
                    @ApiResponse(description = "All Products",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Requested Product was not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(description = "Error while publishing Product",
                            responseCode = "404"
                    )
            })
    void deleteProduct(@PathVariable Integer id);
}
