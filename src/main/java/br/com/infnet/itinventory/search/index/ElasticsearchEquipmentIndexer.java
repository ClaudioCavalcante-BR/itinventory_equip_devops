package br.com.infnet.itinventory.search.index;

import br.com.infnet.itinventory.model.Equipment;
import br.com.infnet.itinventory.repository.EquipmentRepository;
import br.com.infnet.itinventory.search.doc.EquipmentDoc;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.es.enabled", havingValue = "true")
public class ElasticsearchEquipmentIndexer implements EquipmentIndexer {

    private static final String INDEX = "itinv-equipments";

    private final ElasticsearchClient esClient;
    private final EquipmentRepository equipmentRepository;

    @Override
    public void upsert(Long equipmentId) {
        try {
            Equipment eq = equipmentRepository.findById(equipmentId).orElse(null);
            if (eq == null) {
                // Se não existe mais no MySQL, garanta delete no ES para evitar “ghost docs”
                delete(equipmentId);
                return;
            }

            EquipmentDoc doc = toDoc(eq);

            esClient.index(i -> i
                    .index(INDEX)
                    .id(String.valueOf(equipmentId))   // _id do ES = ID do MySQL
                    .document(doc)
            );

        } catch (Exception e) {
            // Não estourar erro para o usuário do CRUD
            log.warn("Falha ao indexar equipment id={} op=UPSERT. Motivo={}", equipmentId, e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long equipmentId) {
        try {
            esClient.delete(d -> d
                    .index(INDEX)
                    .id(String.valueOf(equipmentId))
            );
        } catch (Exception e) {
            log.warn("Falha ao remover equipment id={} op=DELETE. Motivo={}", equipmentId, e.getMessage(), e);
        }
    }
    private EquipmentDoc toDoc(Equipment e) {
        return EquipmentDoc.builder()
                .idEquipment(e.getId())
                .assetNumber(e.getAssetNumber())
                .type(e.getType() != null ? e.getType().name() : null)
                .status(e.getStatus() != null ? e.getStatus().name() : null)
                .brand(e.getBrand())
                .model(e.getModel())
                .location(e.getLocation())
                .responsible(e.getResponsible())
                .acquisitionDate(e.getAcquisitionDate())
                .acquisitionValue(e.getAcquisitionValue() != null ? e.getAcquisitionValue().doubleValue() : null)
                .description(buildDescription(e))
                .ativo(e.getAtivo())
                .criadoEm(e.getCriadoEm())
                .build();
    }

    private String buildDescription(Equipment e) {
        // Campo “full text” opcional para multi_match mais eficiente
        return String.join(" | ",
                safe(e.getAssetNumber()),
                safe(e.getBrand()),
                safe(e.getModel()),
                safe(e.getLocation()),
                safe(e.getResponsible())
        );
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}