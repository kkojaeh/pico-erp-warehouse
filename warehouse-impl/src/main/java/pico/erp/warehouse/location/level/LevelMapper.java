package pico.erp.warehouse.location.level;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.warehouse.location.bay.Bay;
import pico.erp.warehouse.location.bay.BayId;
import pico.erp.warehouse.location.bay.BayMapper;

@org.mapstruct.Mapper
public abstract class LevelMapper {

  @Autowired
  protected BayMapper bayMapper;

  public Level jpa(LevelEntity entity) {
    return Level.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .bay(map(entity.getBayId()))
      .deleted(entity.isDeleted())
      .deletedDate(entity.getDeletedDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "bayId", source = "bay.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract LevelEntity jpa(Level domain);

  @Mappings({
    @Mapping(target = "bayId", source = "bay.id")
  })
  public abstract LevelData map(Level domain);

  public abstract LevelMessages.DeleteRequest map(
    LevelRequests.DeleteRequest request);

  @Mappings({
  })
  public abstract LevelMessages.UpdateRequest map(
    LevelRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "bay", source = "bayId")
  })
  public abstract LevelMessages.CreateRequest map(
    LevelRequests.CreateRequest request);

  protected Bay map(BayId warehouseBayId) {
    return bayMapper.map(warehouseBayId);
  }

  public abstract void pass(LevelEntity from, @MappingTarget LevelEntity to);

}
