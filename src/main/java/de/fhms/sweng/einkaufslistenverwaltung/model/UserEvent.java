package de.fhms.sweng.einkaufslistenverwaltung.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;


@JsonSubTypes({
        @JsonSubTypes.Type(value = UserRegisteredEvent.class, name = "userRegistered"),
        @JsonSubTypes.Type(value = UserDeletedEvent.class, name = "userDeleted"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class UserEvent {

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
