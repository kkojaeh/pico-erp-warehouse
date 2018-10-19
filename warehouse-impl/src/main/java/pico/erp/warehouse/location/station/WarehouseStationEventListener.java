package pico.erp.warehouse.location.station;

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
public class WarehouseStationEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-zone-event-listener";

  @Autowired
  private WarehouseStationRepository warehouseStationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseSiteEvents.DeletedEvent.CHANNEL)
  public void onWarehouseSiteDeleted(WarehouseSiteEvents.DeletedEvent event) {
    warehouseStationRepository.findAllBy(event.getWarehouseSiteId())
      .forEach(station -> {
        val response = station.apply(new WarehouseStationMessages.DeleteRequest());
        warehouseStationRepository.update(station);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseSiteEvents.UpdatedEvent.CHANNEL)
  public void onWarehouseSiteUpdated(WarehouseSiteEvents.UpdatedEvent event) {
    warehouseStationRepository.findAllBy(event.getWarehouseSiteId())
      .forEach(station -> {
        val response = station.apply(new WarehouseStationMessages.ResetLocationCodeRequest());
        warehouseStationRepository.update(station);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
