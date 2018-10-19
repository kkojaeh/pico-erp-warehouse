package pico.erp.warehouse.pack;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.item.lot.ItemLotData;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.WarehouseLocation;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-pack")
public class WarehousePack implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehousePackId id;

  WarehousePackCode code;

  WarehouseLocation location;

  ItemLotData itemLot;

  BigDecimal quantity;

  WarehousePackStatusKind status;

  Auditor createdBy;

  OffsetDateTime createdDate;

  public WarehousePack() {
    status = WarehousePackStatusKind.EMPTY;
  }

  public WarehousePackMessages.CreateResponse apply(
    WarehousePackMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCodeGenerator().generate(this);
    return new WarehousePackMessages.CreateResponse(
      Arrays.asList(new WarehousePackEvents.CreatedEvent(this.id))
    );
  }

  public WarehousePackMessages.PackResponse apply(
    WarehousePackMessages.PackRequest request) {
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    return new WarehousePackMessages.PackResponse(
      Arrays.asList(new WarehousePackEvents.PackedEvent(this.id))
    );
  }

  public WarehousePackMessages.PutResponse apply(
    WarehousePackMessages.PutRequest request) {
    location = request.getLocation();
    return new WarehousePackMessages.PutResponse(
      Arrays.asList(new WarehousePackEvents.PutEvent(this.id))
    );
  }

  public WarehousePackMessages.DeleteResponse apply(
    WarehousePackMessages.DeleteRequest request) {
    return new WarehousePackMessages.DeleteResponse(
      Arrays.asList(new WarehousePackEvents.DeletedEvent(this.id))
    );
  }

}
