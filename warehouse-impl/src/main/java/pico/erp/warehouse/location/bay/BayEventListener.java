package pico.erp.warehouse.location.bay;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.rack.RackEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class BayEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-bay-event-listener";

  @Autowired
  private BayRepository warehouseBayRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + RackEvents.DeletedEvent.CHANNEL)
  public void onRackDeleted(RackEvents.DeletedEvent event) {
    warehouseBayRepository.findAllBy(event.getRackId())
      .forEach(bay -> {
        val response = bay.apply(new BayMessages.DeleteRequest());
        warehouseBayRepository.update(bay);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + RackEvents.UpdatedEvent.CHANNEL)
  public void onRackUpdated(RackEvents.UpdatedEvent event) {
    warehouseBayRepository.findAllBy(event.getRackId())
      .forEach(bay -> {
        val response = bay.apply(new BayMessages.ResetLocationCodeRequest());
        warehouseBayRepository.update(bay);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
