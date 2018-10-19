package pico.erp.warehouse.transaction.request.item;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionRequestItemData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionRequestItemId id;

  WarehouseTransactionRequestId requestId;

  ItemId itemId;

  BigDecimal quantity;

}
