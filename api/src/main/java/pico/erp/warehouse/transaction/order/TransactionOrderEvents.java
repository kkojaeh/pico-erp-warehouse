package pico.erp.warehouse.transaction.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface TransactionOrderEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.created";

    private TransactionOrderId transactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.updated";

    private TransactionOrderId transactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CommittedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.committed";

    private TransactionOrderId transactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CanceledEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.canceled";

    private TransactionOrderId transactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class AcceptedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.accepted";

    private TransactionOrderId transactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CompletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.completed";

    private TransactionOrderId transactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class MemberChangedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-order.member-changed";

    private TransactionOrderId transactionOrderId;

    public String channel() {
      return CHANNEL;
    }

  }

}
