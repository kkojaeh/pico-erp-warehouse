package pico.erp.warehouse.transaction.request.item.lot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface TransactionRequestItemLotEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item-lot.created";

    private TransactionRequestItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item-lot.updated";

    private TransactionRequestItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item-lot.deleted";

    private TransactionRequestItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

}
