package pico.erp.warehouse.location.level;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.bay.BayEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class LevelEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-level-event-listener";

  @Autowired
  private LevelRepository warehouseLevelRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + BayEvents.DeletedEvent.CHANNEL)
  public void onBayDeleted(BayEvents.DeletedEvent event) {
    warehouseLevelRepository.findAllBy(event.getBayId())
      .forEach(level -> {
        val response = level.apply(new LevelMessages.DeleteRequest());
        warehouseLevelRepository.update(level);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + BayEvents.UpdatedEvent.CHANNEL)
  public void onBayUpdated(BayEvents.UpdatedEvent event) {
    warehouseLevelRepository.findAllBy(event.getBayId())
      .forEach(level -> {
        val response = level.apply(new LevelMessages.ResetLocationCodeRequest());
        warehouseLevelRepository.update(level);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
