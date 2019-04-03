package pico.erp.warehouse.location.rack;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.rack.RackExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.rack.RackRequests.CreateRequest;
import pico.erp.warehouse.location.rack.RackRequests.DeleteRequest;
import pico.erp.warehouse.location.rack.RackRequests.UpdateRequest;
import pico.erp.warehouse.location.zone.ZoneExceptions;
import pico.erp.warehouse.location.zone.ZoneId;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class RackServiceLogic implements RackService {

  @Autowired
  private RackRepository rackRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private RackMapper mapper;


  @Override
  public RackData create(CreateRequest request) {
    val warehouseRack = new Rack();
    val response = warehouseRack.apply(mapper.map(request));
    if (rackRepository.exists(warehouseRack.getId())) {
      throw new RackExceptions.AlreadyExistsException();
    }
    if (rackRepository.exists(warehouseRack.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = rackRepository.create(warehouseRack);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val warehouseRack = rackRepository.findBy(request.getId())
      .orElseThrow(RackExceptions.NotFoundException::new);
    val response = warehouseRack.apply(mapper.map(request));
    rackRepository.update(warehouseRack);
    eventPublisher.publishEvents(response.getEvents());
  }

  public void deleteBy(DeleteByZoneRequest request) {
    rackRepository.findAllBy(request.getZoneId())
      .map(rack -> new RackRequests.DeleteRequest(rack.getId()))
      .forEach(this::delete);
  }

  @Override
  public boolean exists(@NotNull RackId id) {
    return rackRepository.exists(id);
  }

  @Override
  public RackData get(@NotNull RackId id) {
    return rackRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(RackExceptions.NotFoundException::new);
  }

  @Override
  public List<RackData> getAll(@NotNull ZoneId zoneId) {
    return rackRepository.findAllBy(zoneId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  public void resetCodeBy(ResetCodeByZoneRequest request) {
    rackRepository.findAllBy(request.getZoneId())
      .forEach(bay -> {
        val response = bay.apply(new RackMessages.ResetLocationCodeRequest());
        rackRepository.update(bay);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseRack = rackRepository.findBy(request.getId())
      .orElseThrow(ZoneExceptions.NotFoundException::new);
    val response = warehouseRack.apply(mapper.map(request));
    if (response.isCodeChanged() && rackRepository
      .exists(warehouseRack.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    rackRepository.update(warehouseRack);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Getter
  @Builder
  public static class DeleteByZoneRequest {

    @Valid
    @NotNull
    ZoneId zoneId;

  }

  @Getter
  @Builder
  public static class ResetCodeByZoneRequest {

    @Valid
    @NotNull
    ZoneId zoneId;

  }
}
