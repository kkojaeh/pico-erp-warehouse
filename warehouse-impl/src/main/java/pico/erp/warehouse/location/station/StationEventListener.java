package pico.erp.warehouse.location.station;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.site.SiteEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class StationEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-zone-event-listener";

  @Autowired
  private StationRepository warehouseStationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + SiteEvents.DeletedEvent.CHANNEL)
  public void onSiteDeleted(SiteEvents.DeletedEvent event) {
    warehouseStationRepository.findAllBy(event.getSiteId())
      .forEach(station -> {
        val response = station.apply(new StationMessages.DeleteRequest());
        warehouseStationRepository.update(station);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + SiteEvents.UpdatedEvent.CHANNEL)
  public void onSiteUpdated(SiteEvents.UpdatedEvent event) {
    warehouseStationRepository.findAllBy(event.getSiteId())
      .forEach(station -> {
        val response = station.apply(new StationMessages.ResetLocationCodeRequest());
        warehouseStationRepository.update(station);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
