package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.ShoppingListProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListProductDto {
    private Integer productId;
    private Integer shoppingListId;
    private Integer amount;

    private String name;
    private Integer bestBeforeTime;
    private Integer price;

    public ShoppingListProductDto(ShoppingListProduct shoppingListProduct) {
        this.productId = shoppingListProduct.getProduct().getId();
        this.shoppingListId = shoppingListProduct.getShoppingList().getId();
        this.amount = shoppingListProduct.getAmount();
        this.name = shoppingListProduct.getProduct().getName();
        this.bestBeforeTime = shoppingListProduct.getProduct().getBestBeforeTime();
        this.price = shoppingListProduct.getProduct().getPrice();
    }
}
