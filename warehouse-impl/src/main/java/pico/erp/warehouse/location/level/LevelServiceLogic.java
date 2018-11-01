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
import pico.erp.warehouse.location.bay.BayId;
import pico.erp.warehouse.location.level.LevelExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.level.LevelRequests.CreateRequest;
import pico.erp.warehouse.location.level.LevelRequests.DeleteRequest;
import pico.erp.warehouse.location.level.LevelRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class LevelServiceLogic implements LevelService {

  @Autowired
  private LevelRepository warehouseLevelRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private LevelMapper mapper;


  @Override
  public LevelData create(CreateRequest request) {
    val level = new Level();
    val response = level.apply(mapper.map(request));
    if (warehouseLevelRepository.exists(level.getId())) {
      throw new LevelExceptions.AlreadyExistsException();
    }
    if (warehouseLevelRepository.exists(level.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = warehouseLevelRepository.create(level);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val level = warehouseLevelRepository.findBy(request.getId())
      .orElseThrow(LevelExceptions.NotFoundException::new);
    val response = level.apply(mapper.map(request));
    warehouseLevelRepository.update(level);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull LevelId id) {
    return warehouseLevelRepository.exists(id);
  }

  @Override
  public LevelData get(@NotNull LevelId id) {
    return warehouseLevelRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(LevelExceptions.NotFoundException::new);
  }

  @Override
  public List<LevelData> getAll(@NotNull BayId bayId) {
    return warehouseLevelRepository.findAllBy(bayId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val level = warehouseLevelRepository.findBy(request.getId())
      .orElseThrow(LevelExceptions.NotFoundException::new);
    val response = level.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseLevelRepository
      .exists(level.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseLevelRepository.update(level);
    eventPublisher.publishEvents(response.getEvents());
  }
}
