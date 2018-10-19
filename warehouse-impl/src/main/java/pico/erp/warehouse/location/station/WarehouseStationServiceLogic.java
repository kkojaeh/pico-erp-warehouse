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
import pico.erp.warehouse.location.site.WarehouseSiteId;
import pico.erp.warehouse.location.station.WarehouseStationExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.station.WarehouseStationRequests.CreateRequest;
import pico.erp.warehouse.location.station.WarehouseStationRequests.DeleteRequest;
import pico.erp.warehouse.location.station.WarehouseStationRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseStationServiceLogic implements WarehouseStationService {

  @Autowired
  private WarehouseStationRepository warehouseStationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private WarehouseStationMapper mapper;

  @Override
  public WarehouseStationData create(CreateRequest request) {
    val station = new WarehouseStation();
    val response = station.apply(mapper.map(request));
    if (warehouseStationRepository.exists(station.getId())) {
      throw new WarehouseStationExceptions.AlreadyExistsException();
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
      .orElseThrow(WarehouseStationExceptions.NotFoundException::new);
    val response = station.apply(mapper.map(request));
    warehouseStationRepository.update(station);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull WarehouseStationId id) {
    return warehouseStationRepository.exists(id);
  }

  @Override
  public WarehouseStationData get(@NotNull WarehouseStationId id) {
    return warehouseStationRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseStationExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseStationData> getAll(@NotNull WarehouseSiteId siteId) {
    return warehouseStationRepository.findAllBy(siteId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val station = warehouseStationRepository.findBy(request.getId())
      .orElseThrow(WarehouseStationExceptions.NotFoundException::new);
    val response = station.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseStationRepository
      .exists(station.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseStationRepository.update(station);
    eventPublisher.publishEvents(response.getEvents());
  }
}
