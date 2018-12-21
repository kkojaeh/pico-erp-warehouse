package pico.erp.warehouse.transaction.request.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface TransactionRequestItemEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item.created";

    private TransactionRequestItemId transactionRequestItemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item.updated";

    private TransactionRequestItemId transactionRequestItemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction-request-item.deleted";

    private TransactionRequestItemId transactionRequestItemId;

    public String channel() {
      return CHANNEL;
    }

  }

}
