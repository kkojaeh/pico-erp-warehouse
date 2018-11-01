package pico.erp.warehouse.location.rack;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.zone.ZoneEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class RackEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-rack-event-listener";

  @Autowired
  private RackRepository warehouseRackRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + ZoneEvents.DeletedEvent.CHANNEL)
  public void onZoneDeleted(ZoneEvents.DeletedEvent event) {
    warehouseRackRepository.findAllBy(event.getZoneId())
      .forEach(rack -> {
        val response = rack.apply(new RackMessages.DeleteRequest());
        warehouseRackRepository.update(rack);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + ZoneEvents.UpdatedEvent.CHANNEL)
  public void onZoneUpdated(ZoneEvents.UpdatedEvent event) {
    warehouseRackRepository.findAllBy(event.getZoneId())
      .forEach(rack -> {
        val response = rack.apply(new RackMessages.ResetLocationCodeRequest());
        warehouseRackRepository.update(rack);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
