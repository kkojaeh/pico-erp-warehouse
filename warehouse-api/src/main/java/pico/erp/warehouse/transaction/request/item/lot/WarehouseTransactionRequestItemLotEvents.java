package pico.erp.warehouse.transaction.request.item.lot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseTransactionRequestItemLotEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item-lot.created";

    private WarehouseTransactionRequestItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item-lot.updated";

    private WarehouseTransactionRequestItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item-lot.deleted";

    private WarehouseTransactionRequestItemLotId warehouseTransactionRequestItemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

}
