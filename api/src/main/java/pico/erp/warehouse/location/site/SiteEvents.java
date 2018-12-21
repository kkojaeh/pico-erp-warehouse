package pico.erp.warehouse.location.site;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface SiteEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-site.created";

    private SiteId siteId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-site.deleted";

    private SiteId siteId;

    public String channel() {
      return CHANNEL;
    }


  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.warehouse-site.updated";

    private SiteId siteId;

    private boolean codeChanged;

    public String channel() {
      return CHANNEL;
    }

  }

}
