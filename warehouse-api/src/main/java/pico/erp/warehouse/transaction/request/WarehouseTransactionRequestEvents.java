package pico.erp.warehouse.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseTransactionRequestEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request.created";

    private WarehouseTransactionRequestId warehouseTransactionRequestId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request.updated";

    private WarehouseTransactionRequestId warehouseTransactionRequestId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CommittedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request.committed";

    private WarehouseTransactionRequestId warehouseTransactionRequestId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CanceledEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request.canceled";

    private WarehouseTransactionRequestId warehouseTransactionRequestId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class AcceptedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request.accepted";

    private WarehouseTransactionRequestId warehouseTransactionRequestId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CompletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request.completed";

    private WarehouseTransactionRequestId warehouseTransactionRequestId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class MemberChangedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request.member-changed";

    private WarehouseTransactionRequestId warehouseTransactionRequestId;

    public String channel() {
      return CHANNEL;
    }

  }


}
