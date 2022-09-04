package de.fhms.sweng.einkaufslistenverwaltung.model.events.user;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdatedEvent extends UserEvent {
    private String name;
    private String email;
    private Role role;

    public UserUpdatedEvent(Integer id, String name, String email, Role role) {
        super(id);
        this.name = name;
        this.email = email;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserUpdatedEvent{" +
                "userId=" + super.getUserId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
