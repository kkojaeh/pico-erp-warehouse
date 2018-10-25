package pico.erp.warehouse.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WarehouseTransactionRequestStatusKind {

  CREATED(true, true, true, false, false),

  COMMITTED(false, false, true, true, false),

  CANCELED(true, false, false, false, false),

  ACCEPTED(false, false, false, false, true),

  COMPLETED(false, false, false, false, false);

  @Getter
  private final boolean modifiable;

  @Getter
  private final boolean committable;

  @Getter
  private final boolean cancelable;

  @Getter
  private final boolean acceptable;

  @Getter
  private final boolean completable;

}
