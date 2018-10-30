package pico.erp.warehouse.location.level;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.warehouse.location.bay.WarehouseBay;
import pico.erp.warehouse.location.bay.WarehouseBayId;
import pico.erp.warehouse.location.bay.WarehouseBayMapper;

@org.mapstruct.Mapper
public abstract class WarehouseLevelMapper {

  @Autowired
  protected WarehouseBayMapper bayMapper;

  public WarehouseLevel jpa(WarehouseLevelEntity entity) {
    return WarehouseLevel.builder()
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
  public abstract WarehouseLevelEntity jpa(WarehouseLevel domain);

  @Mappings({
    @Mapping(target = "bayId", source = "bay.id")
  })
  public abstract WarehouseLevelData map(WarehouseLevel domain);

  public abstract WarehouseLevelMessages.DeleteRequest map(
    WarehouseLevelRequests.DeleteRequest request);

  @Mappings({
  })
  public abstract WarehouseLevelMessages.UpdateRequest map(
    WarehouseLevelRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "bay", source = "bayId")
  })
  public abstract WarehouseLevelMessages.CreateRequest map(
    WarehouseLevelRequests.CreateRequest request);

  protected WarehouseBay map(WarehouseBayId warehouseBayId) {
    return bayMapper.map(warehouseBayId);
  }

  public abstract void pass(WarehouseLevelEntity from, @MappingTarget WarehouseLevelEntity to);

}
