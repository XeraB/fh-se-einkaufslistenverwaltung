package de.fhms.sweng.einkaufslistenverwaltung.inbound;

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
