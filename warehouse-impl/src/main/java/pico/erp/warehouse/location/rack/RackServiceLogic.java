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
import pico.erp.warehouse.location.rack.RackExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.rack.RackRequests.CreateRequest;
import pico.erp.warehouse.location.rack.RackRequests.DeleteRequest;
import pico.erp.warehouse.location.rack.RackRequests.UpdateRequest;
import pico.erp.warehouse.location.zone.ZoneExceptions;
import pico.erp.warehouse.location.zone.ZoneId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class RackServiceLogic implements RackService {

  @Autowired
  private RackRepository warehouseRackRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private RackMapper mapper;


  @Override
  public RackData create(CreateRequest request) {
    val warehouseRack = new Rack();
    val response = warehouseRack.apply(mapper.map(request));
    if (warehouseRackRepository.exists(warehouseRack.getId())) {
      throw new RackExceptions.AlreadyExistsException();
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
      .orElseThrow(RackExceptions.NotFoundException::new);
    val response = warehouseRack.apply(mapper.map(request));
    warehouseRackRepository.update(warehouseRack);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull RackId id) {
    return warehouseRackRepository.exists(id);
  }

  @Override
  public RackData get(@NotNull RackId id) {
    return warehouseRackRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(RackExceptions.NotFoundException::new);
  }

  @Override
  public List<RackData> getAll(@NotNull ZoneId zoneId) {
    return warehouseRackRepository.findAllBy(zoneId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseRack = warehouseRackRepository.findBy(request.getId())
      .orElseThrow(ZoneExceptions.NotFoundException::new);
    val response = warehouseRack.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseRackRepository
      .exists(warehouseRack.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseRackRepository.update(warehouseRack);
    eventPublisher.publishEvents(response.getEvents());
  }
}
