package pico.erp.warehouse.location.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseLevelEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-level.created";

    private WarehouseLevelId warehouseLevelId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-level.deleted";

    private WarehouseLevelId warehouseLevelId;

    public String channel() {
      return CHANNEL;
    }


  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-level.updated";

    private WarehouseLevelId warehouseLevelId;

    private boolean codeChanged;

    public String channel() {
      return CHANNEL;
    }

  }

}
