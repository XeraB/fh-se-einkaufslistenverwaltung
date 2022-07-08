package de.fhms.sweng.einkaufslistenverwaltung.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisteredEvent extends UserEvent {
    private String name;
    private String email;
    private String inviteCode;

    public UserRegisteredEvent(Integer id, String name, String email, String inviteCode) {
        super(id);
        this.name = name;
        this.email = email;
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
}
