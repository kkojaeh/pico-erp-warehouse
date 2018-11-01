package pico.erp.warehouse.transaction.order.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseTransactionOrderItemEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item.created";

    private WarehouseTransactionOrderItemId warehouseTransactionOrderItemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item.updated";

    private WarehouseTransactionOrderItemId warehouseTransactionOrderItemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order-item.deleted";

    private WarehouseTransactionOrderItemId warehouseTransactionOrderItemId;

    public String channel() {
      return CHANNEL;
    }

  }

}
