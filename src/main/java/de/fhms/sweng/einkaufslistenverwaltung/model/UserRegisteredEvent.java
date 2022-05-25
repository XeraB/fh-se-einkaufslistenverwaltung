package de.fhms.sweng.einkaufslistenverwaltung.model;

import java.util.Objects;

public class UserRegisteredEvent extends UserEvent {
    private String name;
    private String email;
    private String inviteCode;

    public UserRegisteredEvent() {
    }

    public UserRegisteredEvent(Integer id, String name, String email, String inviteCode) {
        super(id);
        this.name = name;
        this.email = email;
        this.inviteCode = inviteCode;
    }

    public UserRegisteredEvent(User registeredUser, String inviteCode) {
        super(registeredUser.getId());
        this.name = registeredUser.getName();
        this.email = registeredUser.getEmail();
        this.inviteCode = inviteCode;
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

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public String toString() {
        return "UserRegisteredEvent{" +
                "userId=" + super.getUserId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegisteredEvent)) return false;
        if (!super.equals(o)) return false;
        UserRegisteredEvent that = (UserRegisteredEvent) o;
        return name.equals(that.name) && email.equals(that.email) && inviteCode.equals(that.inviteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, email, inviteCode);
    }
}
