package pico.erp.warehouse.transaction.request.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseTransactionRequestItemEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item.created";

    private WarehouseTransactionRequestItemId warehouseTransactionRequestItemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item.updated";

    private WarehouseTransactionRequestItemId warehouseTransactionRequestItemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item.deleted";

    private WarehouseTransactionRequestItemId warehouseTransactionRequestItemId;

    public String channel() {
      return CHANNEL;
    }

  }

}
