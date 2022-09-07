package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.types.EntryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/rest/shoppingList")
public interface ShoppingListControllerApi {

    String userRole = "hasAnyAuthority('USER','PREMIUM','ADMIN')";

    @PostMapping("/addUser")
    @PreAuthorize(userRole)
    @Operation(summary = "Add a user to a shopping list",
            description = "Adds User to a new shopping list. To add the user to an existing shopping list, add an inviteCode in the RequestBody",
            responses = {
                    @ApiResponse(description = "User was added successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "No ShoppingList found for invite code",
                            responseCode = "404"
                    )
            })
    void addUserToNewOrExistingList(@RequestHeader String Authorization, @RequestBody(required = false) String inviteCode);

    @GetMapping("/all")
    @PreAuthorize(userRole)
    @Operation(summary = "Get all entries from a shopping list by a user id",
            responses = {
                    @ApiResponse(description = "A Set of Entries from the ShoppingList",
                            content = @Content(mediaType = "application/json"),
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Shopping List Not Found",
                            responseCode = "404"
                    )
            })
    Set<ShoppingListProductDto> getAllProductsFromShoppingListByUserId(@RequestHeader String Authorization);

    @PostMapping("/")
    @PreAuthorize(userRole)
    @Operation(summary = "Add a product to a shopping list the product number and amount",
            responses = {
                    @ApiResponse(description = "The new Entry of the ShoppingList",
                            content = @Content(mediaType = "application/json"),
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "No ShoppingList Found for User",
                            responseCode = "404"
                    ),
                    @ApiResponse(description = "Requested Product does not exist",
                            responseCode = "404"
                    ),
                    @ApiResponse(description = "Product already exists on Shopping List",
                            responseCode = "409"
                    )
            })
    ShoppingListProductDto addProductToListByUser(@RequestHeader String Authorization, @RequestBody EntryDto entryDto);

    @PutMapping("/")
    @PreAuthorize(userRole)
    @Operation(summary = "Change the amount of a product on a shopping list",
            responses = {
                    @ApiResponse(description = "The updated Entry of the ShoppingList",
                            content = @Content(mediaType = "application/json"),
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Requested Product is not in Shopping List",
                            responseCode = "404"
                    ),
                    @ApiResponse(description = "No ShoppingList Found for User",
                            responseCode = "404"
                    )
            })
    ShoppingListProductDto updateAmount(@RequestHeader String Authorization, @RequestBody ShoppingListProductDto shoppingListProductDto);

    @DeleteMapping("/food")
    @PreAuthorize(userRole)
    @Operation(summary = "Delete an entry from the shopping list and add the entry to fridge/stock")
    void deleteProductFromListAndSendFoodClient(@RequestHeader String Authorization, @RequestBody EntryDto entryDto);

    @DeleteMapping("/entry")
    @PreAuthorize(userRole)
    @Operation(summary = "Delete an entry from a shopping list",
            responses = {
                    @ApiResponse(description = "The Entry was deleted sucessfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Requested Product is not in Shopping List",
                            responseCode = "404"
                    ),
                    @ApiResponse(description = "No ShoppingList Found for User",
                            responseCode = "404"
                    )
            })
    void deleteProductFromList(@RequestHeader String Authorization, @RequestBody EntryDto entryDto);

    @DeleteMapping("/")
    @PreAuthorize(userRole)
    @Operation(summary = "Remove user from shopping list and delete the list, when no other user left",
            responses = {
                    @ApiResponse(description = "The User was removed sucessfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "No ShoppingList Found for User",
                            responseCode = "404"
                    )
            })
    void deleteShoppingList(@RequestHeader String Authorization);
}
