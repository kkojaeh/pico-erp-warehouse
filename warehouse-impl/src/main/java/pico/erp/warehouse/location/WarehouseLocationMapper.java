package pico.erp.warehouse.location;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pico.erp.warehouse.location.level.WarehouseLevel;
import pico.erp.warehouse.location.level.WarehouseLevelId;

@org.mapstruct.Mapper
public abstract class WarehouseLocationMapper {

  @Mappings({
    @Mapping(target = "code", source = "locationCode")
  })
  public abstract WarehouseLocationData map(WarehouseLevel level);

  public WarehouseLocationId map(WarehouseLevelId id) {
    return WarehouseLocationId.from(id.getValue());
  }

  public WarehouseLevelId map(WarehouseLocationId id) {
    return WarehouseLevelId.from(id.getValue());
  }

}
