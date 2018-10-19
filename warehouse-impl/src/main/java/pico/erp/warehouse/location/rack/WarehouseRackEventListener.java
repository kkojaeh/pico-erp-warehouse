package pico.erp.warehouse.location.rack;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.zone.WarehouseZoneEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseRackEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-rack-event-listener";

  @Autowired
  private WarehouseRackRepository warehouseRackRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseZoneEvents.DeletedEvent.CHANNEL)
  public void onWarehouseZoneDeleted(WarehouseZoneEvents.DeletedEvent event) {
    warehouseRackRepository.findAllBy(event.getWarehouseZoneId())
      .forEach(rack -> {
        val response = rack.apply(new WarehouseRackMessages.DeleteRequest());
        warehouseRackRepository.update(rack);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseZoneEvents.UpdatedEvent.CHANNEL)
  public void onWarehouseZoneUpdated(WarehouseZoneEvents.UpdatedEvent event) {
    warehouseRackRepository.findAllBy(event.getWarehouseZoneId())
      .forEach(rack -> {
        val response = rack.apply(new WarehouseRackMessages.ResetLocationCodeRequest());
        warehouseRackRepository.update(rack);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
