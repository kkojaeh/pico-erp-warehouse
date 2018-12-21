package pico.erp.warehouse.transaction.order.item;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class TransactionOrderItemData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionOrderItemId id;

  TransactionOrderId orderId;

  ItemId itemId;

  BigDecimal quantity;

}
