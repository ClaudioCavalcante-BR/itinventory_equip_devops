package br.com.infnet.itinventory.search.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentDoc {

    private Long idEquipment;

    private String assetNumber;
    private String type;
    private String status;

    private String brand;
    private String model;

    private String location;
    private String responsible;

    private LocalDate acquisitionDate;
    private Double acquisitionValue;

    // Campo opcional para busca full-text “unificada”
    private String description;

    private Boolean ativo;
    private LocalDateTime criadoEm;

    //private OffsetDateTime criadoEm;
}