package pico.erp.warehouse.location.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.warehouse.location.site.SiteEvents;

@SuppressWarnings("unused")
@Component
public class StationEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-zone-event-listener";

  @Autowired
  private StationServiceLogic stationService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + SiteEvents.DeletedEvent.CHANNEL)
  public void onSiteDeleted(SiteEvents.DeletedEvent event) {
    stationService.deleteBy(
      StationServiceLogic.DeleteBySiteRequest.builder()
        .siteId(event.getSiteId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + SiteEvents.UpdatedEvent.CHANNEL)
  public void onSiteUpdated(SiteEvents.UpdatedEvent event) {
    stationService.resetCodeBy(
      StationServiceLogic.ResetCodeBySiteRequest.builder()
        .siteId(event.getSiteId())
        .build()
    );
  }

}
