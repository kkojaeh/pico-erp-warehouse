package pico.erp.warehouse.pack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehousePackEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-pack.created";

    private WarehousePackId warehousePackId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-pack.deleted";

    private WarehousePackId warehousePackId;

    public String channel() {
      return CHANNEL;
    }


  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class PackedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-pack.packed";

    private WarehousePackId warehousePackId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class PutEvent implements Event {

    public final static String CHANNEL = "event.warehouse-pack.put";

    private WarehousePackId warehousePackId;

    public String channel() {
      return CHANNEL;
    }

  }

}
