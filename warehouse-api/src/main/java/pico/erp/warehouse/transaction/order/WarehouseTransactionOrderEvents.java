package pico.erp.warehouse.transaction.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseTransactionOrderEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.created";

    private WarehouseTransactionOrderId warehouseTransactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.updated";

    private WarehouseTransactionOrderId warehouseTransactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CommittedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.committed";

    private WarehouseTransactionOrderId warehouseTransactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CanceledEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.canceled";

    private WarehouseTransactionOrderId warehouseTransactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class AcceptedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.accepted";

    private WarehouseTransactionOrderId warehouseTransactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CompletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.completed";

    private WarehouseTransactionOrderId warehouseTransactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class MemberChangedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.member-changed";

    private WarehouseTransactionOrderId warehouseTransactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

}
