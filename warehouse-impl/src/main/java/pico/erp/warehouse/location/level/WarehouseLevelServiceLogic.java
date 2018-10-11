package pico.erp.warehouse.location.level;

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
import pico.erp.warehouse.location.bay.WarehouseBayId;
import pico.erp.warehouse.location.level.WarehouseLevelExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.level.WarehouseLevelRequests.CreateRequest;
import pico.erp.warehouse.location.level.WarehouseLevelRequests.DeleteRequest;
import pico.erp.warehouse.location.level.WarehouseLevelRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseLevelServiceLogic implements WarehouseLevelService {

  @Autowired
  private WarehouseLevelRepository warehouseLevelRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private WarehouseLevelMapper mapper;


  @Override
  public WarehouseLevelData create(CreateRequest request) {
    val warehouseLevel = new WarehouseLevel();
    val response = warehouseLevel.apply(mapper.map(request));
    if (warehouseLevelRepository.exists(warehouseLevel.getId())) {
      throw new WarehouseLevelExceptions.AlreadyExistsException();
    }
    if (warehouseLevelRepository.exists(warehouseLevel.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = warehouseLevelRepository.create(warehouseLevel);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val warehouseLevel = warehouseLevelRepository.findBy(request.getId())
      .orElseThrow(WarehouseLevelExceptions.NotFoundException::new);
    val response = warehouseLevel.apply(mapper.map(request));
    warehouseLevelRepository.update(warehouseLevel);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull WarehouseLevelId id) {
    return warehouseLevelRepository.exists(id);
  }

  @Override
  public WarehouseLevelData get(@NotNull WarehouseLevelId id) {
    return warehouseLevelRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseLevelExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseLevelData> getAll(@NotNull WarehouseBayId bayId) {
    return warehouseLevelRepository.findAllBy(bayId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseLevel = warehouseLevelRepository.findBy(request.getId())
      .orElseThrow(WarehouseLevelExceptions.NotFoundException::new);
    val response = warehouseLevel.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseLevelRepository
      .exists(warehouseLevel.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseLevelRepository.update(warehouseLevel);
    eventPublisher.publishEvents(response.getEvents());
  }
}
