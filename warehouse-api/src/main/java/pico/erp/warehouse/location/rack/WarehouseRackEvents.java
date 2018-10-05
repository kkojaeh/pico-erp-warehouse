package pico.erp.warehouse.location.rack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseRackEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-rack.created";

    private WarehouseRackId warehouseRackId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-rack.deleted";

    private WarehouseRackId warehouseRackId;

    public String channel() {
      return CHANNEL;
    }


  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-rack.updated";

    private WarehouseRackId warehouseRackId;

    private boolean codeChanged;

    public String channel() {
      return CHANNEL;
    }

  }

}
