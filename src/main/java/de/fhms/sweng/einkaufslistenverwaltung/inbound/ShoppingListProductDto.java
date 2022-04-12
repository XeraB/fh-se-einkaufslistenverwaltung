package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListProduct;

public class ShoppingListProductDto {
    private Integer productId;
    private Integer shoppingListId;
    private Integer amount;

    public ShoppingListProductDto() {
    }

    public ShoppingListProductDto(Integer productId, Integer shoppingListId, Integer amount) {
        this.productId = productId;
        this.shoppingListId = shoppingListId;
        this.amount = amount;
    }

    public ShoppingListProductDto(ShoppingListProduct shoppingListProduct) {
        this.productId = shoppingListProduct.getProduct().getId();
        this.shoppingListId = shoppingListProduct.getShoppingList().getId();
        this.amount = shoppingListProduct.getAmount();
    }


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Integer shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
