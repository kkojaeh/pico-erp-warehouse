package pico.erp.warehouse.location.site;

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
import pico.erp.warehouse.location.site.WarehouseSiteExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.site.WarehouseSiteRequests.CreateRequest;
import pico.erp.warehouse.location.site.WarehouseSiteRequests.DeleteRequest;
import pico.erp.warehouse.location.site.WarehouseSiteRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseSiteServiceLogic implements WarehouseSiteService {

  @Autowired
  private WarehouseSiteRepository warehouseSiteRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private WarehouseSiteMapper mapper;

  @Override
  public WarehouseSiteData create(CreateRequest request) {
    val warehouseSite = new WarehouseSite();
    val response = warehouseSite.apply(mapper.map(request));
    if (warehouseSiteRepository.exists(warehouseSite.getId())) {
      throw new WarehouseSiteExceptions.AlreadyExistsException();
    }
    if (warehouseSiteRepository.exists(warehouseSite.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = warehouseSiteRepository.create(warehouseSite);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val warehouseSite = warehouseSiteRepository.findBy(request.getId())
      .orElseThrow(WarehouseSiteExceptions.NotFoundException::new);
    val response = warehouseSite.apply(mapper.map(request));
    warehouseSiteRepository.update(warehouseSite);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull WarehouseSiteId id) {
    return warehouseSiteRepository.exists(id);
  }

  @Override
  public WarehouseSiteData get(@NotNull WarehouseSiteId id) {
    return warehouseSiteRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseSiteExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseSiteData> getAll() {
    return warehouseSiteRepository.findAll()
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseSite = warehouseSiteRepository.findBy(request.getId())
      .orElseThrow(WarehouseSiteExceptions.NotFoundException::new);
    val response = warehouseSite.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseSiteRepository
      .exists(warehouseSite.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseSiteRepository.update(warehouseSite);
    eventPublisher.publishEvents(response.getEvents());
  }
}
