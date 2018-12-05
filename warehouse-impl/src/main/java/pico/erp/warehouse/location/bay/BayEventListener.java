package pico.erp.warehouse.location.bay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.warehouse.location.rack.RackEvents;

@SuppressWarnings("unused")
@Component
public class BayEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-bay-event-listener";

  @Autowired
  private BayServiceLogic bayService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + RackEvents.DeletedEvent.CHANNEL)
  public void onRackDeleted(RackEvents.DeletedEvent event) {
    bayService.deleteBy(
      BayServiceLogic.DeleteByRackRequest.builder()
        .rackId(event.getRackId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + RackEvents.UpdatedEvent.CHANNEL)
  public void onRackUpdated(RackEvents.UpdatedEvent event) {
    bayService.resetCodeBy(
      BayServiceLogic.ResetCodeByRackRequest.builder()
        .rackId(event.getRackId())
        .build()
    );
  }

}
