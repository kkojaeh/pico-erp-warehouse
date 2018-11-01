package pico.erp.warehouse.transaction.order.item.lot;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionOrderItemLotData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionOrderItemLotId id;

  WarehouseTransactionOrderItemId orderItemId;

  ItemLotId itemLotId;

  BigDecimal quantity;

}
