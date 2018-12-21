package pico.erp.warehouse.location.zone;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.site.Site;
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.site.SiteMapper;

@org.mapstruct.Mapper
public abstract class ZoneMapper {

  @Autowired
  protected SiteMapper siteMapper;

  @Lazy
  @Autowired
  protected ZoneRepository warehouseZoneRepository;

  public Zone jpa(ZoneEntity entity) {
    return Zone.builder()
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
  public abstract ZoneEntity jpa(Zone domain);

  @Mappings({
  })
  public abstract ZoneMessages.UpdateRequest map(
    ZoneRequests.UpdateRequest request);

  public abstract ZoneMessages.DeleteRequest map(
    ZoneRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "siteId", source = "site.id")
  })
  public abstract ZoneData map(Zone domain);

  public Zone map(ZoneId warehouseZoneId) {
    return Optional.ofNullable(warehouseZoneId)
      .map(id -> warehouseZoneRepository.findBy(id)
        .orElseThrow(ZoneExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "site", source = "siteId")
  })
  public abstract ZoneMessages.CreateRequest map(
    ZoneRequests.CreateRequest request);

  protected Site map(SiteId warehouseSiteId) {
    return siteMapper.map(warehouseSiteId);
  }

  public abstract void pass(ZoneEntity from, @MappingTarget ZoneEntity to);


}
