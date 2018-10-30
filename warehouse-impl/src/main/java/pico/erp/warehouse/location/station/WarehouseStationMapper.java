package pico.erp.warehouse.location.station;

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
public abstract class WarehouseStationMapper {

  @Autowired
  protected WarehouseSiteMapper siteMapper;

  @Lazy
  @Autowired
  protected WarehouseStationRepository warehouseStationRepository;

  public WarehouseStation jpa(WarehouseStationEntity entity) {
    return WarehouseStation.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .name(entity.getName())
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
  public abstract WarehouseStationEntity jpa(WarehouseStation domain);

  @Mappings({
  })
  public abstract WarehouseStationMessages.UpdateRequest map(
    WarehouseStationRequests.UpdateRequest request);

  public abstract WarehouseStationMessages.DeleteRequest map(
    WarehouseStationRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "siteId", source = "site.id")
  })
  public abstract WarehouseStationData map(WarehouseStation domain);

  public WarehouseStation map(WarehouseStationId warehouseStationId) {
    return Optional.ofNullable(warehouseStationId)
      .map(id -> warehouseStationRepository.findBy(id)
        .orElseThrow(WarehouseStationExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "site", source = "siteId")
  })
  public abstract WarehouseStationMessages.CreateRequest map(
    WarehouseStationRequests.CreateRequest request);

  protected WarehouseSite map(WarehouseSiteId warehouseSiteId) {
    return siteMapper.map(warehouseSiteId);
  }

  public abstract void pass(WarehouseStationEntity from, @MappingTarget WarehouseStationEntity to);


}
