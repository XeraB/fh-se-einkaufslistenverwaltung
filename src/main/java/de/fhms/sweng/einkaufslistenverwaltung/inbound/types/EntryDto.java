package de.fhms.sweng.einkaufslistenverwaltung.inbound.types;

import de.fhms.sweng.einkaufslistenverwaltung.model.types.Unit;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EntryDto {
    private Integer productId;
    private Integer amount;
    private Unit unit;
}
