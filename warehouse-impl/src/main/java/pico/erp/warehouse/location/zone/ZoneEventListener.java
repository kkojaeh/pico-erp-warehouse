package pico.erp.warehouse.location.zone;

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
public class ZoneEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-zone-event-listener";

  @Autowired
  private ZoneRepository warehouseZoneRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + SiteEvents.DeletedEvent.CHANNEL)
  public void onSiteDeleted(SiteEvents.DeletedEvent event) {
    warehouseZoneRepository.findAllBy(event.getSiteId())
      .forEach(zone -> {
        val response = zone.apply(new ZoneMessages.DeleteRequest());
        warehouseZoneRepository.update(zone);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + SiteEvents.UpdatedEvent.CHANNEL)
  public void onSiteUpdated(SiteEvents.UpdatedEvent event) {
    warehouseZoneRepository.findAllBy(event.getSiteId())
      .forEach(zone -> {
        val response = zone.apply(new ZoneMessages.ResetLocationCodeRequest());
        warehouseZoneRepository.update(zone);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
