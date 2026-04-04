package br.com.infnet.itinventory.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_equipment" )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, name= "type" )
    private EquipmentType type; //Ex.: NOTEBOOK, DESKTOP, MONITOR, SERVIDOR, ROTEADOR, IMPRESSORA

    @Column(nullable = false, length = 100, name = "brand")
    private String brand; // marca Dell, HP, Lenovo, LG, Cisco etc.

    @Column(nullable = false, length = 150, name = "model")
    private String model;  // “Latitude 5420”, “ProLiant DL380”

    // INV- + 5 dígitos = 9 caracteres
    @Column(name = "asset_number", length = 9, unique = true)
    private String assetNumber; // Ex.: INV-00013

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, name= "status")
    private EquipmentStatus status; // EM_USO, EM_MANUTENCAO, ESTOQUE, DESCARTADO

    @Column(nullable = false, length = 150)
    private String location; //“Matriz – Financeiro”, “Filial – SP – TI”

    @Column(nullable = false, length = 150, name = "responsible")
    private String responsible;  // Nome do responsável

    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    @Column(name = "acquisition_value", precision = 15, scale = 2)
    private BigDecimal acquisitionValue;



    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    private void prePersist() {
        normalizeAssetNumber(); // garante padronização na criação
        if (ativo == null) ativo = true;
        if (criadoEm == null) criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        normalizeAssetNumber(); // garante padronização na atualização
        atualizadoEm = LocalDateTime.now();
    }

    //@PrePersist
    //@PreUpdate
    private void normalizeAssetNumber() {
        if (assetNumber == null) return;

        String s = assetNumber.trim().toUpperCase();

        // aceita AAA-1..AAA-99999
        if (!s.matches("^[A-Z]{3}-\\d{1,5}$")) {
            return; // deixa o Bean Validation tratar se estiver inválido
        }

        String prefix = s.substring(0, 3);
        String digits = s.substring(4);

        int n = Integer.parseInt(digits); // remove zeros à esquerda
        this.assetNumber = prefix + "-" + String.format("%05d", n);
    }
}