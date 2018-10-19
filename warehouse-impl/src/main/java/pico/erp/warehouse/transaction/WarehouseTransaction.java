package pico.erp.warehouse.transaction;

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

@Builder
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction")
public class WarehouseTransaction implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionId id;

  ItemLotData itemLot;

  BigDecimal quantity;

  WarehouseTransactionTypeKind type;

  Auditor transactedBy;

  OffsetDateTime transactedDate;

  public WarehouseTransaction() {

  }

  public WarehouseTransactionMessages.InboundResponse apply(
    WarehouseTransactionMessages.InboundRequest request) {
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    type = WarehouseTransactionTypeKind.INBOUND;
    return new WarehouseTransactionMessages.InboundResponse(
      Arrays.asList(new WarehouseTransactionEvents.InboundedEvent(this.id))
    );
  }

  public WarehouseTransactionMessages.OutboundResponse apply(
    WarehouseTransactionMessages.OutboundRequest request) {
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    type = WarehouseTransactionTypeKind.OUTBOUND;
    return new WarehouseTransactionMessages.OutboundResponse(
      Arrays.asList(new WarehouseTransactionEvents.OutboundedEvent(this.id))
    );
  }

}
