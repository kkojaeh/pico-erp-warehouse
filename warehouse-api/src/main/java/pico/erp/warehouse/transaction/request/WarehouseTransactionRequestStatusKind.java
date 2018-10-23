package pico.erp.warehouse.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WarehouseTransactionRequestStatusKind {

  CREATED(true, true, true),

  COMMITTED(false, false, true),

  CANCELED(true, false, false),

  IN_PROGRESS(false, false, false),

  COMPLETED(false, false, false);

  @Getter
  private final boolean modifiable;

  @Getter
  private final boolean committable;

  @Getter
  private final boolean cancelable;

}
