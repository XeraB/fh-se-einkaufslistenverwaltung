package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingList;

public class ShoppingListDto {

    private Integer id;
    private Integer userId;

    public ShoppingListDto() {
    }

    public ShoppingListDto(Integer id, Integer userId) {
        this.id = id;
        this.userId = userId;
    }

    public ShoppingListDto(ShoppingList shoppingList) {
        this.id = shoppingList.getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
