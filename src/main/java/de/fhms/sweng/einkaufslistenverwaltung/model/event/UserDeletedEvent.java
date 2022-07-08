package de.fhms.sweng.einkaufslistenverwaltung.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDeletedEvent extends UserEvent {

    private String name;
    private String email;


    public UserDeletedEvent(Integer userId, String name, String email) {
        super(userId);
        this.name = name;
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
}
