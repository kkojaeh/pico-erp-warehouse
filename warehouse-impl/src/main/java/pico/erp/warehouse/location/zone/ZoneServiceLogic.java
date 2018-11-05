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
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.zone.ZoneExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.zone.ZoneRequests.CreateRequest;
import pico.erp.warehouse.location.zone.ZoneRequests.DeleteRequest;
import pico.erp.warehouse.location.zone.ZoneRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class ZoneServiceLogic implements ZoneService {

  @Autowired
  private ZoneRepository warehouseZoneRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private ZoneMapper mapper;

  @Override
  public ZoneData create(CreateRequest request) {
    val warehouseZone = new Zone();
    val response = warehouseZone.apply(mapper.map(request));
    if (warehouseZoneRepository.exists(warehouseZone.getId())) {
      throw new ZoneExceptions.AlreadyExistsException();
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
      .orElseThrow(ZoneExceptions.NotFoundException::new);
    val response = warehouseZone.apply(mapper.map(request));
    warehouseZoneRepository.update(warehouseZone);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull ZoneId id) {
    return warehouseZoneRepository.exists(id);
  }

  @Override
  public ZoneData get(@NotNull ZoneId id) {
    return warehouseZoneRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(ZoneExceptions.NotFoundException::new);
  }

  @Override
  public List<ZoneData> getAll(@NotNull SiteId siteId) {
    return warehouseZoneRepository.findAllBy(siteId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseZone = warehouseZoneRepository.findBy(request.getId())
      .orElseThrow(ZoneExceptions.NotFoundException::new);
    val response = warehouseZone.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseZoneRepository
      .exists(warehouseZone.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseZoneRepository.update(warehouseZone);
    eventPublisher.publishEvents(response.getEvents());
  }
}
