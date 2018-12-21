package pico.erp.warehouse.transaction.request.item;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.request.TransactionRequestId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class TransactionRequestItemData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionRequestItemId id;

  TransactionRequestId requestId;

  ItemId itemId;

  BigDecimal quantity;

}
