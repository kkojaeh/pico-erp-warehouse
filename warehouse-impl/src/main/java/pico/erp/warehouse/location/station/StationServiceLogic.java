package pico.erp.warehouse.location.station;

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
import pico.erp.warehouse.location.station.StationExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.station.StationRequests.CreateRequest;
import pico.erp.warehouse.location.station.StationRequests.DeleteRequest;
import pico.erp.warehouse.location.station.StationRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class StationServiceLogic implements StationService {

  @Autowired
  private StationRepository warehouseStationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private StationMapper mapper;

  @Override
  public StationData create(CreateRequest request) {
    val station = new Station();
    val response = station.apply(mapper.map(request));
    if (warehouseStationRepository.exists(station.getId())) {
      throw new StationExceptions.AlreadyExistsException();
    }
    if (warehouseStationRepository.exists(station.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = warehouseStationRepository.create(station);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val station = warehouseStationRepository.findBy(request.getId())
      .orElseThrow(StationExceptions.NotFoundException::new);
    val response = station.apply(mapper.map(request));
    warehouseStationRepository.update(station);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull StationId id) {
    return warehouseStationRepository.exists(id);
  }

  @Override
  public StationData get(@NotNull StationId id) {
    return warehouseStationRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(StationExceptions.NotFoundException::new);
  }

  @Override
  public List<StationData> getAll(@NotNull SiteId siteId) {
    return warehouseStationRepository.findAllBy(siteId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val station = warehouseStationRepository.findBy(request.getId())
      .orElseThrow(StationExceptions.NotFoundException::new);
    val response = station.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseStationRepository
      .exists(station.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseStationRepository.update(station);
    eventPublisher.publishEvents(response.getEvents());
  }
}
