package pico.erp.warehouse.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.level.WarehouseLevelEvents;
import pico.erp.warehouse.location.level.WarehouseLevelRepository;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseLocationEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-location-event-listener";

  @Autowired
  private WarehouseLevelRepository warehouseLevelRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseLevelEvents.CreatedEvent.CHANNEL)
  public void onWarehouseLevelCreated(WarehouseLevelEvents.CreatedEvent event) {
    eventPublisher.publishEvent(
      new WarehouseLocationEvents.CreatedEvent(
        WarehouseLocationId.from(event.getWarehouseLevelId().getValue()))
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseLevelEvents.DeletedEvent.CHANNEL)
  public void onWarehouseLevelDeleted(WarehouseLevelEvents.DeletedEvent event) {
    eventPublisher.publishEvent(
      new WarehouseLocationEvents.DeletedEvent(
        WarehouseLocationId.from(event.getWarehouseLevelId().getValue()))
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseLevelEvents.UpdatedEvent.CHANNEL)
  public void onWarehouseLevelUpdated(WarehouseLevelEvents.UpdatedEvent event) {
    eventPublisher.publishEvent(
      new WarehouseLocationEvents.UpdatedEvent(
        WarehouseLocationId.from(event.getWarehouseLevelId().getValue()))
    );
  }

}
