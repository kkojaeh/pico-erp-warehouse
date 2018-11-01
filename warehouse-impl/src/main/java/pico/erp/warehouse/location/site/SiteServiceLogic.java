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
import pico.erp.warehouse.location.site.SiteExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.site.SiteRequests.CreateRequest;
import pico.erp.warehouse.location.site.SiteRequests.DeleteRequest;
import pico.erp.warehouse.location.site.SiteRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class SiteServiceLogic implements SiteService {

  @Autowired
  private SiteRepository warehouseSiteRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private SiteMapper mapper;

  @Override
  public SiteData create(CreateRequest request) {
    val warehouseSite = new Site();
    val response = warehouseSite.apply(mapper.map(request));
    if (warehouseSiteRepository.exists(warehouseSite.getId())) {
      throw new SiteExceptions.AlreadyExistsException();
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
      .orElseThrow(SiteExceptions.NotFoundException::new);
    val response = warehouseSite.apply(mapper.map(request));
    warehouseSiteRepository.update(warehouseSite);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull SiteId id) {
    return warehouseSiteRepository.exists(id);
  }

  @Override
  public SiteData get(@NotNull SiteId id) {
    return warehouseSiteRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(SiteExceptions.NotFoundException::new);
  }

  @Override
  public List<SiteData> getAll() {
    return warehouseSiteRepository.findAll()
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseSite = warehouseSiteRepository.findBy(request.getId())
      .orElseThrow(SiteExceptions.NotFoundException::new);
    val response = warehouseSite.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseSiteRepository
      .exists(warehouseSite.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseSiteRepository.update(warehouseSite);
    eventPublisher.publishEvents(response.getEvents());
  }
}
