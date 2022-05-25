package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.Objects;

public class UserDeletedEvent extends UserEvent {

    private String name;
    private String email;

    public UserDeletedEvent() {
    }

    public UserDeletedEvent(Integer userId, String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDeletedEvent{" +
                "userId=" + super.getUserId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDeletedEvent)) return false;
        if (!super.equals(o)) return false;
        UserDeletedEvent that = (UserDeletedEvent) o;
        return name.equals(that.name) && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, email);
    }
}
