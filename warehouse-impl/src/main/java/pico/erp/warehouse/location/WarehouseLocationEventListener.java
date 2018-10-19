package pico.erp.warehouse.location;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.level.WarehouseLevelEvents;
import pico.erp.warehouse.location.level.WarehouseLevelExceptions;
import pico.erp.warehouse.location.level.WarehouseLevelRepository;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseLocationEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-location-event-listener";

  @Autowired
  private WarehouseLevelRepository warehouseLevelRepository;

  @Autowired
  private WarehouseLocationRepository warehouseLocationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseLevelEvents.CreatedEvent.CHANNEL)
  public void onWarehouseLevelCreated(WarehouseLevelEvents.CreatedEvent event) {
    val level = warehouseLevelRepository.findBy(event.getWarehouseLevelId())
      .orElseThrow(WarehouseLevelExceptions.NotFoundException::new);
    val location = new WarehouseLocation();
    val response = location.apply(
      WarehouseLocationMessages.CreateRequest.builder()
        .id(WarehouseLocationId.from(level.getId().getValue()))
        .code(level.getLocationCode())
        .build()
    );
    warehouseLocationRepository.create(location);
    eventPublisher.publishEvents(response.getEvents());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseLevelEvents.DeletedEvent.CHANNEL)
  public void onWarehouseLevelDeleted(WarehouseLevelEvents.DeletedEvent event) {
    val location = warehouseLocationRepository
      .findBy(WarehouseLocationId.from(event.getWarehouseLevelId().getValue()))
      .orElseThrow(WarehouseLocationExceptions.NotFoundException::new);
    val response = location.apply(new WarehouseLocationMessages.DeleteRequest());
    warehouseLocationRepository.update(location);
    eventPublisher.publishEvents(response.getEvents());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + WarehouseLevelEvents.UpdatedEvent.CHANNEL)
  public void onWarehouseLevelUpdated(WarehouseLevelEvents.UpdatedEvent event) {
    val level = warehouseLevelRepository.findBy(event.getWarehouseLevelId())
      .orElseThrow(WarehouseLevelExceptions.NotFoundException::new);
    val location = warehouseLocationRepository
      .findBy(WarehouseLocationId.from(event.getWarehouseLevelId().getValue()))
      .orElseThrow(WarehouseLocationExceptions.NotFoundException::new);
    val response = location.apply(
      WarehouseLocationMessages.UpdateRequest.builder()
        .code(level.getLocationCode())
        .build()
    );
    if (response.isCodeChanged() && warehouseLocationRepository
      .exists(location.getCode())) {
      throw new WarehouseLocationExceptions.CodeAlreadyExistsException();
    }
    warehouseLocationRepository.update(location);
    eventPublisher.publishEvents(response.getEvents());
  }

}
