package pico.erp.warehouse.location.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface StationEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-station.created";

    private StationId warehouseStationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-station.deleted";

    private StationId warehouseStationId;

    public String channel() {
      return CHANNEL;
    }


  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-station.updated";

    private StationId warehouseStationId;

    private boolean codeChanged;

    public String channel() {
      return CHANNEL;
    }

  }

}
