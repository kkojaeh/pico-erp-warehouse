package pico.erp.warehouse.location;

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
import pico.erp.warehouse.location.level.LevelId;
import pico.erp.warehouse.location.level.LevelService;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class LocationServiceLogic implements LocationService {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private LevelService levelService;

  public LocationData createBy(CreateByLevelRequest request) {
    val level = levelService.get(request.getLevelId());
    val location = new Location();
    val response = location.apply(
      LocationMessages.CreateRequest.builder()
        .id(LocationId.from(level.getId().getValue()))
        .code(level.getLocationCode())
        .build()
    );
    val created = locationRepository.create(location);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  public void deleteBy(DeleteByLevelRequest request) {
    val location = locationRepository
      .findBy(LocationId.from(request.getLevelId().getValue()))
      .orElseThrow(LocationExceptions.NotFoundException::new);
    val response = location.apply(new LocationMessages.DeleteRequest());
    locationRepository.update(location);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull LocationId id) {
    return locationRepository.exists(id);
  }

  @Override
  public LocationData get(@NotNull LocationId id) {
    return locationRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(LocationExceptions.NotFoundException::new);
  }

  public void updateBy(UpdateByLevelRequest request) {
    val level = levelService.get(request.getLevelId());
    val location = locationRepository
      .findBy(LocationId.from(request.getLevelId().getValue()))
      .orElseThrow(LocationExceptions.NotFoundException::new);
    val response = location.apply(
      LocationMessages.UpdateRequest.builder()
        .code(level.getLocationCode())
        .build()
    );
    if (response.isCodeChanged() && locationRepository
      .exists(location.getCode())) {
      throw new LocationExceptions.CodeAlreadyExistsException();
    }
    locationRepository.update(location);
    eventPublisher.publishEvents(response.getEvents());
  }


  @Getter
  @Builder
  public static class CreateByLevelRequest {

    @Valid
    @NotNull
    LevelId levelId;

  }

  @Getter
  @Builder
  public static class DeleteByLevelRequest {

    @Valid
    @NotNull
    LevelId levelId;

  }

  @Getter
  @Builder
  public static class UpdateByLevelRequest {

    @Valid
    @NotNull
    LevelId levelId;

  }
}
