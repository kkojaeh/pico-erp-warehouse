package pico.erp.warehouse.transaction.order.item.lot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface TransactionOrderItemLotEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item-lot.created";

    private TransactionOrderItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item-lot.updated";

    private TransactionOrderItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item-lot.deleted";

    private TransactionOrderItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

}
