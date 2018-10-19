package pico.erp.warehouse.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseTransactionEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class InboundedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction.inbounded";

    private WarehouseTransactionId warehouseTransactionId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class OutboundedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-transaction.outbounded";

    private WarehouseTransactionId warehouseTransactionId;

    public String channel() {
      return CHANNEL;
    }


  }


}
