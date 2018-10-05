package pico.erp.warehouse.location.site;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface WarehouseSiteEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-site.created";

    private WarehouseSiteId warehouseSiteId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-site.deleted";

    private WarehouseSiteId warehouseSiteId;

    public String channel() {
      return CHANNEL;
    }


  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-site.updated";

    private WarehouseSiteId warehouseSiteId;

    private boolean codeChanged;

    public String channel() {
      return CHANNEL;
    }

  }

}
