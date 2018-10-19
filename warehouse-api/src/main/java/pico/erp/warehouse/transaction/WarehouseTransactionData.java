package pico.erp.warehouse.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.item.lot.ItemLotId;
import pico.erp.shared.data.Auditor;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionData implements Serializable {

  private static final long serialVersionUID = 1L;

  WarehouseTransactionId id;

  ItemLotId itemLotId;

  BigDecimal quantity;

  Auditor transactedBy;

  OffsetDateTime transactedDate;

  //WarehouseTransactionRequestId requestId;

}
