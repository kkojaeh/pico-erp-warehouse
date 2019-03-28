package pico.erp.warehouse.location.site;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import kkojaeh.spring.boot.component.Give;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.site.SiteExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.site.SiteRequests.CreateRequest;
import pico.erp.warehouse.location.site.SiteRequests.DeleteRequest;
import pico.erp.warehouse.location.site.SiteRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Give
@Transactional
@Validated
public class SiteServiceLogic implements SiteService {

  @Autowired
  private SiteRepository siteRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private SiteMapper mapper;

  @Override
  public SiteData create(CreateRequest request) {
    val warehouseSite = new Site();
    val response = warehouseSite.apply(mapper.map(request));
    if (siteRepository.exists(warehouseSite.getId())) {
      throw new SiteExceptions.AlreadyExistsException();
    }
    if (siteRepository.exists(warehouseSite.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = siteRepository.create(warehouseSite);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val warehouseSite = siteRepository.findBy(request.getId())
      .orElseThrow(SiteExceptions.NotFoundException::new);
    val response = warehouseSite.apply(mapper.map(request));
    siteRepository.update(warehouseSite);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull SiteId id) {
    return siteRepository.exists(id);
  }

  @Override
  public SiteData get(@NotNull SiteId id) {
    return siteRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(SiteExceptions.NotFoundException::new);
  }

  @Override
  public List<SiteData> getAll() {
    return siteRepository.findAll()
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseSite = siteRepository.findBy(request.getId())
      .orElseThrow(SiteExceptions.NotFoundException::new);
    val response = warehouseSite.apply(mapper.map(request));
    if (response.isCodeChanged() && siteRepository
      .exists(warehouseSite.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    siteRepository.update(warehouseSite);
    eventPublisher.publishEvents(response.getEvents());
  }
}
