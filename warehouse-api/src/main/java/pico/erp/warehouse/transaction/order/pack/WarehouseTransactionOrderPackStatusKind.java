package pico.erp.warehouse.transaction.order.pack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;

@AllArgsConstructor
public enum WarehouseTransactionOrderPackStatusKind {

  INBOUND_READY(WarehouseTransactionTypeKind.INBOUND),

  INBOUND_COMPLETED(WarehouseTransactionTypeKind.INBOUND),

  OUTBOUND_READY(WarehouseTransactionTypeKind.OUTBOUND),

  OUTBOUND_COMPLETED(WarehouseTransactionTypeKind.OUTBOUND);

  @Getter
  WarehouseTransactionTypeKind type;

  public static WarehouseTransactionOrderPackStatusKind ready(WarehouseTransactionTypeKind type) {
    if (type == WarehouseTransactionTypeKind.INBOUND) {
      return INBOUND_READY;
    }
    return OUTBOUND_READY;
  }

}
