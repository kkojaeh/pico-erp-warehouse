package pico.erp.warehouse.location;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.level.LevelId;

@org.mapstruct.Mapper
public abstract class LocationMapper {


  @Lazy
  @Autowired
  protected LocationRepository locationRepository;

  public LocationId id(LevelId id) {
    return LocationId.from(id.getValue());
  }

  public LevelId id(LocationId id) {
    return LevelId.from(id.getValue());
  }

  public Location jpa(LocationEntity entity) {
    return Location.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .deleted(entity.isDeleted())
      .deletedDate(entity.getDeletedDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract LocationEntity jpa(Location domain);

  @Mappings({
  })
  public abstract LocationData map(Location level);

  public Location map(LocationId locationId) {
    return Optional.ofNullable(locationId)
      .map(id -> locationRepository.findBy(id)
        .orElseThrow(LocationExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  public abstract void pass(LocationEntity from,
    @MappingTarget LocationEntity to);

}
