package pico.erp.warehouse.location.level;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.bay.WarehouseBayEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseLevelEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-level-event-listener";

  @Autowired
  private WarehouseLevelRepository warehouseLevelRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseBayEvents.UpdatedEvent.CHANNEL)
  public void onWarehouseBayUpdated(WarehouseBayEvents.UpdatedEvent event) {
    warehouseLevelRepository.findAllBy(event.getWarehouseBayId())
      .forEach(level -> {
        val response = level.apply(new WarehouseLevelMessages.ResetLocationCodeRequest());
        warehouseLevelRepository.update(level);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseBayEvents.DeletedEvent.CHANNEL)
  public void onWarehouseBayDeleted(WarehouseBayEvents.DeletedEvent event) {
    warehouseLevelRepository.findAllBy(event.getWarehouseBayId())
      .forEach(level -> {
        val response = level.apply(new WarehouseLevelMessages.DeleteRequest());
        warehouseLevelRepository.update(level);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
