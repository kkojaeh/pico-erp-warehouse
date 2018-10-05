package pico.erp.warehouse.location.level;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.warehouse.location.bay.WarehouseBayMapper;

@org.mapstruct.Mapper
public abstract class WarehouseLevelMapper {

  @Autowired
  protected WarehouseBayMapper bayMapper;

  public WarehouseLevel domain(WarehouseLevelEntity entity) {
    return WarehouseLevel.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .bay(bayMapper.domain(entity.getBay()))
      .build();
  }

  @Mappings({
    @Mapping(target = "bay", expression = "java(bayMapper.entity(domain.getBay()))"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract WarehouseLevelEntity entity(WarehouseLevel domain);

  public abstract WarehouseLevelMessages.DeleteRequest map(
    WarehouseLevelRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "bayId", source = "bay.id")
  })
  public abstract WarehouseLevelData map(WarehouseLevel domain);

  @Mappings({
    @Mapping(target = "bay", expression = "java(bayMapper.map(request.getBayId()))")
  })
  public abstract WarehouseLevelMessages.CreateRequest map(
    WarehouseLevelRequests.CreateRequest request);

  @Mappings({
  })
  public abstract WarehouseLevelMessages.UpdateRequest map(
    WarehouseLevelRequests.UpdateRequest request);

  public abstract void pass(WarehouseLevelEntity from, @MappingTarget WarehouseLevelEntity to);

}
