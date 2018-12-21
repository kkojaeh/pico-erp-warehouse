package pico.erp.warehouse.location.rack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.warehouse.location.zone.ZoneEvents;

@SuppressWarnings("unused")
@Component
public class RackEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-rack-event-listener";

  @Autowired
  private RackRepository warehouseRackRepository;

  @Autowired
  private RackServiceLogic rackService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + ZoneEvents.DeletedEvent.CHANNEL)
  public void onZoneDeleted(ZoneEvents.DeletedEvent event) {
    rackService.deleteBy(
      RackServiceLogic.DeleteByZoneRequest.builder()
        .zoneId(event.getZoneId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + ZoneEvents.UpdatedEvent.CHANNEL)
  public void onZoneUpdated(ZoneEvents.UpdatedEvent event) {
    rackService.resetCodeBy(
      RackServiceLogic.ResetCodeByZoneRequest.builder()
        .zoneId(event.getZoneId())
        .build()
    );
  }

}
