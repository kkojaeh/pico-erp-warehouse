package pico.erp.warehouse.location.zone;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.site.WarehouseSiteEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseZoneEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-zone-event-listener";

  @Autowired
  private WarehouseZoneRepository warehouseZoneRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseSiteEvents.DeletedEvent.CHANNEL)
  public void onWarehouseSiteDeleted(WarehouseSiteEvents.DeletedEvent event) {
    warehouseZoneRepository.findAllBy(event.getWarehouseSiteId())
      .forEach(zone -> {
        val response = zone.apply(new WarehouseZoneMessages.DeleteRequest());
        warehouseZoneRepository.update(zone);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseSiteEvents.UpdatedEvent.CHANNEL)
  public void onWarehouseSiteUpdated(WarehouseSiteEvents.UpdatedEvent event) {
    warehouseZoneRepository.findAllBy(event.getWarehouseSiteId())
      .forEach(zone -> {
        val response = zone.apply(new WarehouseZoneMessages.ResetLocationCodeRequest());
        warehouseZoneRepository.update(zone);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
