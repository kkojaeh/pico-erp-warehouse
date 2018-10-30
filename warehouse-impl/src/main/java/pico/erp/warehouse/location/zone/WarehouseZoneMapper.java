package pico.erp.warehouse.location.zone;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.site.WarehouseSite;
import pico.erp.warehouse.location.site.WarehouseSiteId;
import pico.erp.warehouse.location.site.WarehouseSiteMapper;

@org.mapstruct.Mapper
public abstract class WarehouseZoneMapper {

  @Autowired
  protected WarehouseSiteMapper siteMapper;

  @Lazy
  @Autowired
  protected WarehouseZoneRepository warehouseZoneRepository;

  public WarehouseZone jpa(WarehouseZoneEntity entity) {
    return WarehouseZone.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .site(map(entity.getSiteId()))
      .deleted(entity.isDeleted())
      .deletedDate(entity.getDeletedDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "siteId", source = "site.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract WarehouseZoneEntity jpa(WarehouseZone domain);

  @Mappings({
  })
  public abstract WarehouseZoneMessages.UpdateRequest map(
    WarehouseZoneRequests.UpdateRequest request);

  public abstract WarehouseZoneMessages.DeleteRequest map(
    WarehouseZoneRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "siteId", source = "site.id")
  })
  public abstract WarehouseZoneData map(WarehouseZone domain);

  public WarehouseZone map(WarehouseZoneId warehouseZoneId) {
    return Optional.ofNullable(warehouseZoneId)
      .map(id -> warehouseZoneRepository.findBy(id)
        .orElseThrow(WarehouseZoneExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "site", source = "siteId")
  })
  public abstract WarehouseZoneMessages.CreateRequest map(
    WarehouseZoneRequests.CreateRequest request);

  protected WarehouseSite map(WarehouseSiteId warehouseSiteId) {
    return siteMapper.map(warehouseSiteId);
  }

  public abstract void pass(WarehouseZoneEntity from, @MappingTarget WarehouseZoneEntity to);


}
