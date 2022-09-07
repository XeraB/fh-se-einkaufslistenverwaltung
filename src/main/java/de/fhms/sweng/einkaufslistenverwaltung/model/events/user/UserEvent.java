package de.fhms.sweng.einkaufslistenverwaltung.model.events.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserRegisteredEvent.class, name = "userRegistered"),
        @JsonSubTypes.Type(value = UserUpdatedEvent.class, name = "userUpdated"),
        @JsonSubTypes.Type(value = UserDeletedEvent.class, name = "userDeleted"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class UserEvent {

    private Integer userId;

}
