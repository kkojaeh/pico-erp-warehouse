package pico.erp.warehouse.transaction.order.pack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseTransactionOrderPackEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item.created";

    private WarehouseTransactionOrderPackId warehouseTransactionOrderPackId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item.updated";

    private WarehouseTransactionOrderPackId warehouseTransactionOrderPackId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item.deleted";

    private WarehouseTransactionOrderPackId warehouseTransactionOrderPackId;

    public String channel() {
      return CHANNEL;
    }

  }

}
