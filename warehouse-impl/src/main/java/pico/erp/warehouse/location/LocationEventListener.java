package pico.erp.warehouse.location;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.level.LevelEvents;
import pico.erp.warehouse.location.level.LevelExceptions;
import pico.erp.warehouse.location.level.LevelRepository;

@SuppressWarnings("unused")
@Component
@Transactional
public class LocationEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-location-event-listener";

  @Autowired
  private LevelRepository warehouseLevelRepository;

  @Autowired
  private LocationRepository warehouseLocationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + LevelEvents.CreatedEvent.CHANNEL)
  public void onLevelCreated(LevelEvents.CreatedEvent event) {
    val level = warehouseLevelRepository.findBy(event.getLevelId())
      .orElseThrow(LevelExceptions.NotFoundException::new);
    val location = new Location();
    val response = location.apply(
      LocationMessages.CreateRequest.builder()
        .id(LocationId.from(level.getId().getValue()))
        .code(level.getLocationCode())
        .build()
    );
    warehouseLocationRepository.create(location);
    eventPublisher.publishEvents(response.getEvents());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + LevelEvents.DeletedEvent.CHANNEL)
  public void onLevelDeleted(LevelEvents.DeletedEvent event) {
    val location = warehouseLocationRepository
      .findBy(LocationId.from(event.getLevelId().getValue()))
      .orElseThrow(LocationExceptions.NotFoundException::new);
    val response = location.apply(new LocationMessages.DeleteRequest());
    warehouseLocationRepository.update(location);
    eventPublisher.publishEvents(response.getEvents());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + LevelEvents.UpdatedEvent.CHANNEL)
  public void onLevelUpdated(LevelEvents.UpdatedEvent event) {
    val level = warehouseLevelRepository.findBy(event.getLevelId())
      .orElseThrow(LevelExceptions.NotFoundException::new);
    val location = warehouseLocationRepository
      .findBy(LocationId.from(event.getLevelId().getValue()))
      .orElseThrow(LocationExceptions.NotFoundException::new);
    val response = location.apply(
      LocationMessages.UpdateRequest.builder()
        .code(level.getLocationCode())
        .build()
    );
    if (response.isCodeChanged() && warehouseLocationRepository
      .exists(location.getCode())) {
      throw new LocationExceptions.CodeAlreadyExistsException();
    }
    warehouseLocationRepository.update(location);
    eventPublisher.publishEvents(response.getEvents());
  }

}
