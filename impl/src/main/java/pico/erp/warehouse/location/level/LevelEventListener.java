package pico.erp.warehouse.location.level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.warehouse.location.bay.BayEvents;

@SuppressWarnings("unused")
@Component
public class LevelEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-level-event-listener";

  @Autowired
  private LevelServiceLogic levelService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + BayEvents.DeletedEvent.CHANNEL)
  public void onBayDeleted(BayEvents.DeletedEvent event) {
    levelService.deleteBy(
      LevelServiceLogic.DeleteByBayRequest.builder()
        .bayId(event.getBayId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + BayEvents.UpdatedEvent.CHANNEL)
  public void onBayUpdated(BayEvents.UpdatedEvent event) {
    levelService.resetCodeBy(
      LevelServiceLogic.ResetCodeByBayRequest.builder()
        .bayId(event.getBayId())
        .build()
    );
  }

}
