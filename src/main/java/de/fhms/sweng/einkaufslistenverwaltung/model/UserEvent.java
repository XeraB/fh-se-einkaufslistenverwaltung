package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.Objects;

public class UserEvent {

    private Integer userId;

    public UserEvent() {
    }

    public UserEvent(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEvent userEvent = (UserEvent) o;
        return userId.equals(userEvent.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
