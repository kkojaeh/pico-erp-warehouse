package pico.erp.warehouse.transaction.order.pack;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.pack.PackId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class TransactionOrderPackData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionOrderPackId id;

  TransactionOrderId orderId;

  PackId packId;

  TransactionOrderPackStatusKind status;

}
