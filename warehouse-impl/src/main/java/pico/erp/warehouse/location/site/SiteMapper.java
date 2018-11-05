package pico.erp.warehouse.location.site;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@org.mapstruct.Mapper
public abstract class SiteMapper {

  @Lazy
  @Autowired
  protected SiteRepository warehouseSiteRepository;

  public Site jpa(SiteEntity entity) {
    return Site.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .name(entity.getName())
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
  public abstract SiteEntity jpa(Site domain);

  @Mappings({
  })
  public abstract SiteMessages.UpdateRequest map(
    SiteRequests.UpdateRequest request);

  public abstract SiteMessages.DeleteRequest map(
    SiteRequests.DeleteRequest request);

  public abstract SiteData map(Site domain);

  public Site map(SiteId warehouseSiteId) {
    return Optional.ofNullable(warehouseSiteId)
      .map(id -> warehouseSiteRepository.findBy(id)
        .orElseThrow(SiteExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
  })
  public abstract SiteMessages.CreateRequest map(
    SiteRequests.CreateRequest request);

  public abstract void pass(SiteEntity from, @MappingTarget SiteEntity to);

}
