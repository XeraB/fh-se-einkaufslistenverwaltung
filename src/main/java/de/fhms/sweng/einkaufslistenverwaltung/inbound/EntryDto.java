package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import java.util.Objects;

public class EntryDto {
    private Integer productId;
    private Integer userId;
    private Integer amount;

    public EntryDto() {
    }

    public EntryDto(Integer productId, Integer userId, Integer amount) {
        this.productId = productId;
        this.userId = userId;
        this.amount = amount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "EntryDto{" +
                "productId=" + productId +
                ", userId=" + userId +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryDto entryDto = (EntryDto) o;
        return productId.equals(entryDto.productId) && userId.equals(entryDto.userId) && amount.equals(entryDto.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userId, amount);
    }
}
