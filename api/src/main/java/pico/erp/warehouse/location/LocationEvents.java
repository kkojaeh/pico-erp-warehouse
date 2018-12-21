package pico.erp.warehouse.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface LocationEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-location.created";

    private LocationId warehouseLocationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-location.deleted";

    private LocationId warehouseLocationId;

    public String channel() {
      return CHANNEL;
    }


  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-location.updated";

    private LocationId warehouseLocationId;

    private boolean codeChanged;

    public String channel() {
      return CHANNEL;
    }

  }

}
