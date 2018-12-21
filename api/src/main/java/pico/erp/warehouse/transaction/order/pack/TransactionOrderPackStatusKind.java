package pico.erp.warehouse.transaction.order.pack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.shared.data.LocalizedNameable;
import pico.erp.warehouse.transaction.TransactionTypeKind;

@AllArgsConstructor
public enum TransactionOrderPackStatusKind implements LocalizedNameable {

  INBOUND_READY(TransactionTypeKind.INBOUND),

  INBOUND_COMPLETED(TransactionTypeKind.INBOUND),

  OUTBOUND_READY(TransactionTypeKind.OUTBOUND),

  OUTBOUND_COMPLETED(TransactionTypeKind.OUTBOUND);

  @Getter
  TransactionTypeKind type;

  public static TransactionOrderPackStatusKind ready(TransactionTypeKind type) {
    if (type == TransactionTypeKind.INBOUND) {
      return INBOUND_READY;
    }
    return OUTBOUND_READY;
  }

}
