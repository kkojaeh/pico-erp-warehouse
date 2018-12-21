package pico.erp.warehouse.location.level;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
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
  private LevelRepository levelRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private LevelMapper mapper;


  @Override
  public LevelData create(CreateRequest request) {
    val level = new Level();
    val response = level.apply(mapper.map(request));
    if (levelRepository.exists(level.getId())) {
      throw new LevelExceptions.AlreadyExistsException();
    }
    if (levelRepository.exists(level.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = levelRepository.create(level);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val level = levelRepository.findBy(request.getId())
      .orElseThrow(LevelExceptions.NotFoundException::new);
    val response = level.apply(mapper.map(request));
    levelRepository.update(level);
    eventPublisher.publishEvents(response.getEvents());
  }

  public void deleteBy(DeleteByBayRequest request) {
    levelRepository.findAllBy(request.getBayId())
      .map(level -> new LevelRequests.DeleteRequest(level.getId()))
      .forEach(this::delete);
  }

  @Override
  public boolean exists(@NotNull LevelId id) {
    return levelRepository.exists(id);
  }

  @Override
  public LevelData get(@NotNull LevelId id) {
    return levelRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(LevelExceptions.NotFoundException::new);
  }

  @Override
  public List<LevelData> getAll(@NotNull BayId bayId) {
    return levelRepository.findAllBy(bayId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  public void resetCodeBy(ResetCodeByBayRequest request) {
    levelRepository.findAllBy(request.getBayId())
      .forEach(bay -> {
        val response = bay.apply(new LevelMessages.ResetLocationCodeRequest());
        levelRepository.update(bay);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void update(UpdateRequest request) {
    val level = levelRepository.findBy(request.getId())
      .orElseThrow(LevelExceptions.NotFoundException::new);
    val response = level.apply(mapper.map(request));
    if (response.isCodeChanged() && levelRepository
      .exists(level.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    levelRepository.update(level);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Getter
  @Builder
  public static class DeleteByBayRequest {

    @Valid
    @NotNull
    BayId bayId;

  }

  @Getter
  @Builder
  public static class ResetCodeByBayRequest {

    @Valid
    @NotNull
    BayId bayId;

  }
}
