package pico.erp.warehouse.location.zone;

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
import pico.erp.warehouse.location.site.WarehouseSiteId;
import pico.erp.warehouse.location.zone.WarehouseZoneExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.zone.WarehouseZoneRequests.CreateRequest;
import pico.erp.warehouse.location.zone.WarehouseZoneRequests.DeleteRequest;
import pico.erp.warehouse.location.zone.WarehouseZoneRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseZoneServiceLogic implements WarehouseZoneService {

  @Autowired
  private WarehouseZoneRepository warehouseZoneRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private WarehouseZoneMapper mapper;

  @Override
  public WarehouseZoneData create(CreateRequest request) {
    val warehouseZone = new WarehouseZone();
    val response = warehouseZone.apply(mapper.map(request));
    if (warehouseZoneRepository.exists(warehouseZone.getId())) {
      throw new WarehouseZoneExceptions.AlreadyExistsException();
    }
    if (warehouseZoneRepository.exists(warehouseZone.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = warehouseZoneRepository.create(warehouseZone);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val warehouseZone = warehouseZoneRepository.findBy(request.getId())
      .orElseThrow(WarehouseZoneExceptions.NotFoundException::new);
    val response = warehouseZone.apply(mapper.map(request));
    warehouseZoneRepository.update(warehouseZone);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull WarehouseZoneId id) {
    return warehouseZoneRepository.exists(id);
  }

  @Override
  public WarehouseZoneData get(@NotNull WarehouseZoneId id) {
    return warehouseZoneRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseZoneExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseZoneData> getAll(@NotNull WarehouseSiteId siteId) {
    return warehouseZoneRepository.findAllBy(siteId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseZone = warehouseZoneRepository.findBy(request.getId())
      .orElseThrow(WarehouseZoneExceptions.NotFoundException::new);
    val response = warehouseZone.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseZoneRepository
      .exists(warehouseZone.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseZoneRepository.update(warehouseZone);
    eventPublisher.publishEvents(response.getEvents());
  }
}
