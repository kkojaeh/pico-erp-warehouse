package pico.erp.warehouse.transaction.request.item.lot;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionRequestItemLotData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionRequestItemLotId id;

  WarehouseTransactionRequestItemId requestItemId;

  ItemLotId itemLotId;

  BigDecimal quantity;

}
