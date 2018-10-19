package pico.erp.warehouse.location;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.level.WarehouseLevelId;

@org.mapstruct.Mapper
public abstract class WarehouseLocationMapper {


  @Lazy
  @Autowired
  protected WarehouseLocationRepository locationRepository;

  public WarehouseLocationId id(WarehouseLevelId id) {
    return WarehouseLocationId.from(id.getValue());
  }

  public WarehouseLevelId id(WarehouseLocationId id) {
    return WarehouseLevelId.from(id.getValue());
  }

  public WarehouseLocation jpa(WarehouseLocationEntity entity) {
    return WarehouseLocation.builder()
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
  public abstract WarehouseLocationEntity jpa(WarehouseLocation domain);

  @Mappings({
  })
  public abstract WarehouseLocationData map(WarehouseLocation level);

  public WarehouseLocation map(WarehouseLocationId locationId) {
    return Optional.ofNullable(locationId)
      .map(id -> locationRepository.findBy(id)
        .orElseThrow(WarehouseLocationExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  public abstract void pass(WarehouseLocationEntity from,
    @MappingTarget WarehouseLocationEntity to);

}
