package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListProduct;

public class ShoppingListProductDto {
    private Integer productId;
    private Integer shoppingListId;
    private Integer amount;

    private String name;
    private Integer bestBeforeTime;
    private Integer price;

    public ShoppingListProductDto() {
    }

    public ShoppingListProductDto(Integer productId, Integer shoppingListId, Integer amount, String name, Integer bestBeforeTime, Integer price) {
        this.productId = productId;
        this.shoppingListId = shoppingListId;
        this.amount = amount;
        this.name = name;
        this.bestBeforeTime = bestBeforeTime;
        this.price = price;
    }

    public ShoppingListProductDto(ShoppingListProduct shoppingListProduct) {
        this.productId = shoppingListProduct.getProduct().getId();
        this.shoppingListId = shoppingListProduct.getShoppingList().getId();
        this.amount = shoppingListProduct.getAmount();
        this.name = shoppingListProduct.getProduct().getName();
        this.bestBeforeTime = shoppingListProduct.getProduct().getBestBeforeTime();
        this.price = shoppingListProduct.getProduct().getPrice();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBestBeforeTime() {
        return bestBeforeTime;
    }

    public void setBestBeforeTime(Integer bestBeforeTime) {
        this.bestBeforeTime = bestBeforeTime;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
