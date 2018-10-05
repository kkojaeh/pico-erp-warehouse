package pico.erp.warehouse.location.bay;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.rack.WarehouseRackEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseBayEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-bay-event-listener";

  @Autowired
  private WarehouseBayRepository warehouseBayRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseRackEvents.UpdatedEvent.CHANNEL)
  public void onWarehouseRackUpdated(WarehouseRackEvents.UpdatedEvent event) {
    warehouseBayRepository.findAllBy(event.getWarehouseRackId())
      .forEach(bay -> {
        val response = bay.apply(new WarehouseBayMessages.ResetLocationCodeRequest());
        warehouseBayRepository.update(bay);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
