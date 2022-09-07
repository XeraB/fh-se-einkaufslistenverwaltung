package de.fhms.sweng.einkaufslistenverwaltung.inbound.types;

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
}
