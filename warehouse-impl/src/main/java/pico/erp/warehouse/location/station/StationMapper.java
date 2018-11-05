package pico.erp.warehouse.location.station;

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
public abstract class StationMapper {

  @Autowired
  protected SiteMapper siteMapper;

  @Lazy
  @Autowired
  protected StationRepository warehouseStationRepository;

  public Station jpa(StationEntity entity) {
    return Station.builder()
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
  public abstract StationEntity jpa(Station domain);

  @Mappings({
  })
  public abstract StationMessages.UpdateRequest map(
    StationRequests.UpdateRequest request);

  public abstract StationMessages.DeleteRequest map(
    StationRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "siteId", source = "site.id")
  })
  public abstract StationData map(Station domain);

  public Station map(StationId warehouseStationId) {
    return Optional.ofNullable(warehouseStationId)
      .map(id -> warehouseStationRepository.findBy(id)
        .orElseThrow(StationExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "site", source = "siteId")
  })
  public abstract StationMessages.CreateRequest map(
    StationRequests.CreateRequest request);

  protected Site map(SiteId warehouseSiteId) {
    return siteMapper.map(warehouseSiteId);
  }

  public abstract void pass(StationEntity from, @MappingTarget StationEntity to);


}
