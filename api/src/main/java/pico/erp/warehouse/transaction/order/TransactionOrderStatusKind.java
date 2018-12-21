package pico.erp.warehouse.transaction.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.shared.data.LocalizedNameable;

@AllArgsConstructor
public enum TransactionOrderStatusKind implements LocalizedNameable {

  CREATED(true, true, true, false, false),

  COMMITTED(false, false, true, true, false),

  CANCELED(false, false, false, false, false),

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
