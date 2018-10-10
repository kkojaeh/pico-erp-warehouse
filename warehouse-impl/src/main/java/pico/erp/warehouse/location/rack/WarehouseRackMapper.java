package pico.erp.warehouse.location.rack;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.zone.WarehouseZone;
import pico.erp.warehouse.location.zone.WarehouseZoneEntity;
import pico.erp.warehouse.location.zone.WarehouseZoneId;
import pico.erp.warehouse.location.zone.WarehouseZoneMapper;

@org.mapstruct.Mapper
public abstract class WarehouseRackMapper {

  @Autowired
  protected WarehouseZoneMapper zoneMapper;

  @Lazy
  @Autowired
  protected WarehouseRackRepository warehouseRackRepository;

  public WarehouseRack jpa(WarehouseRackEntity entity) {
    return WarehouseRack.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .zone(zoneMapper.jpa(entity.getZone()))
      .build();
  }

  @Mappings({
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract WarehouseRackEntity jpa(WarehouseRack domain);

  @Mappings({
  })
  public abstract WarehouseRackMessages.UpdateRequest map(
    WarehouseRackRequests.UpdateRequest request);

  public abstract WarehouseRackMessages.DeleteRequest map(
    WarehouseRackRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "zoneId", source = "zone.id")
  })
  public abstract WarehouseRackData map(WarehouseRack domain);

  public WarehouseRack map(WarehouseRackId warehouseRackId) {
    return Optional.ofNullable(warehouseRackId)
      .map(id -> warehouseRackRepository.findBy(id)
        .orElseThrow(WarehouseRackExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected WarehouseZoneEntity jpa(WarehouseZone domain) {
    return zoneMapper.jpa(domain);
  }

  public abstract void pass(WarehouseRackEntity from, @MappingTarget WarehouseRackEntity to);

  protected WarehouseZone map(WarehouseZoneId warehouseZoneId) {
    return zoneMapper.map(warehouseZoneId);
  }

  @Mappings({
    @Mapping(target = "zone", source = "zoneId")
  })
  public abstract WarehouseRackMessages.CreateRequest map(
    WarehouseRackRequests.CreateRequest request);

}
