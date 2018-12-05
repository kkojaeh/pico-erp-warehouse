package pico.erp.warehouse.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.warehouse.location.level.LevelEvents;

@SuppressWarnings("unused")
@Component
public class LocationEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-location-event-listener";

  @Autowired
  private LocationServiceLogic locationService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + LevelEvents.CreatedEvent.CHANNEL)
  public void onLevelCreated(LevelEvents.CreatedEvent event) {
    locationService.createBy(
      LocationServiceLogic.CreateByLevelRequest.builder()
        .levelId(event.getLevelId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + LevelEvents.DeletedEvent.CHANNEL)
  public void onLevelDeleted(LevelEvents.DeletedEvent event) {
    locationService.deleteBy(
      LocationServiceLogic.DeleteByLevelRequest.builder()
        .levelId(event.getLevelId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + LevelEvents.UpdatedEvent.CHANNEL)
  public void onLevelUpdated(LevelEvents.UpdatedEvent event) {
    locationService.updateBy(
      LocationServiceLogic.UpdateByLevelRequest.builder()
        .levelId(event.getLevelId())
        .build()
    );
  }

}
