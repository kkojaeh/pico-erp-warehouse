package pico.erp.warehouse.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface TransactionEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class InboundedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction.inbounded";

    private TransactionId warehouseTransactionId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class OutboundedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction.outbounded";

    private TransactionId warehouseTransactionId;

    public String channel() {
      return CHANNEL;
    }


  }


}
