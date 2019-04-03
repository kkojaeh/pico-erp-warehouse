package pico.erp.warehouse.location.zone;

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
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.zone.ZoneExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.zone.ZoneRequests.CreateRequest;
import pico.erp.warehouse.location.zone.ZoneRequests.DeleteRequest;
import pico.erp.warehouse.location.zone.ZoneRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class ZoneServiceLogic implements ZoneService {

  @Autowired
  private ZoneRepository zoneRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private ZoneMapper mapper;

  @Override
  public ZoneData create(CreateRequest request) {
    val warehouseZone = new Zone();
    val response = warehouseZone.apply(mapper.map(request));
    if (zoneRepository.exists(warehouseZone.getId())) {
      throw new ZoneExceptions.AlreadyExistsException();
    }
    if (zoneRepository.exists(warehouseZone.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = zoneRepository.create(warehouseZone);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val warehouseZone = zoneRepository.findBy(request.getId())
      .orElseThrow(ZoneExceptions.NotFoundException::new);
    val response = warehouseZone.apply(mapper.map(request));
    zoneRepository.update(warehouseZone);
    eventPublisher.publishEvents(response.getEvents());
  }

  public void deleteBy(DeleteBySiteRequest request) {
    zoneRepository.findAllBy(request.getSiteId())
      .map(level -> new ZoneRequests.DeleteRequest(level.getId()))
      .forEach(this::delete);
  }

  @Override
  public boolean exists(@NotNull ZoneId id) {
    return zoneRepository.exists(id);
  }

  @Override
  public ZoneData get(@NotNull ZoneId id) {
    return zoneRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(ZoneExceptions.NotFoundException::new);
  }

  @Override
  public List<ZoneData> getAll(@NotNull SiteId siteId) {
    return zoneRepository.findAllBy(siteId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  public void resetCodeBy(ResetCodeBySiteRequest request) {
    zoneRepository.findAllBy(request.getSiteId())
      .forEach(bay -> {
        val response = bay.apply(new ZoneMessages.ResetLocationCodeRequest());
        zoneRepository.update(bay);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseZone = zoneRepository.findBy(request.getId())
      .orElseThrow(ZoneExceptions.NotFoundException::new);
    val response = warehouseZone.apply(mapper.map(request));
    if (response.isCodeChanged() && zoneRepository
      .exists(warehouseZone.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    zoneRepository.update(warehouseZone);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Getter
  @Builder
  public static class DeleteBySiteRequest {

    @Valid
    @NotNull
    SiteId siteId;

  }

  @Getter
  @Builder
  public static class ResetCodeBySiteRequest {

    @Valid
    @NotNull
    SiteId siteId;

  }
}
