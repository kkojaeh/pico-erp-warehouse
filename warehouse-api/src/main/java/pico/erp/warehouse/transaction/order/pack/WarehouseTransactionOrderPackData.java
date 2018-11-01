package pico.erp.warehouse.transaction.order.pack;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.pack.WarehousePackId;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionOrderPackData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionOrderPackId id;

  WarehouseTransactionOrderId orderId;

  WarehousePackId packId;

  WarehouseTransactionOrderPackStatusKind status;

}
