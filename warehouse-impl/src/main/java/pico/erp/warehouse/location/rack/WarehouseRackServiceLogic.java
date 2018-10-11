package pico.erp.warehouse.location.rack;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.rack.WarehouseRackExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.rack.WarehouseRackRequests.CreateRequest;
import pico.erp.warehouse.location.rack.WarehouseRackRequests.DeleteRequest;
import pico.erp.warehouse.location.rack.WarehouseRackRequests.UpdateRequest;
import pico.erp.warehouse.location.zone.WarehouseZoneExceptions;
import pico.erp.warehouse.location.zone.WarehouseZoneId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseRackServiceLogic implements WarehouseRackService {

  @Autowired
  private WarehouseRackRepository warehouseRackRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private WarehouseRackMapper mapper;


  @Override
  public WarehouseRackData create(CreateRequest request) {
    val warehouseRack = new WarehouseRack();
    val response = warehouseRack.apply(mapper.map(request));
    if (warehouseRackRepository.exists(warehouseRack.getId())) {
      throw new WarehouseRackExceptions.AlreadyExistsException();
    }
    if (warehouseRackRepository.exists(warehouseRack.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = warehouseRackRepository.create(warehouseRack);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val warehouseRack = warehouseRackRepository.findBy(request.getId())
      .orElseThrow(WarehouseRackExceptions.NotFoundException::new);
    val response = warehouseRack.apply(mapper.map(request));
    warehouseRackRepository.update(warehouseRack);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull WarehouseRackId id) {
    return warehouseRackRepository.exists(id);
  }

  @Override
  public WarehouseRackData get(@NotNull WarehouseRackId id) {
    return warehouseRackRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseRackExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseRackData> getAll(@NotNull WarehouseZoneId zoneId) {
    return warehouseRackRepository.findAllBy(zoneId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseRack = warehouseRackRepository.findBy(request.getId())
      .orElseThrow(WarehouseZoneExceptions.NotFoundException::new);
    val response = warehouseRack.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseRackRepository
      .exists(warehouseRack.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseRackRepository.update(warehouseRack);
    eventPublisher.publishEvents(response.getEvents());
  }
}
