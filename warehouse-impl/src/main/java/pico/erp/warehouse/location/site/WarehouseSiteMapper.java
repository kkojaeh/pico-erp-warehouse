package pico.erp.warehouse.location.site;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@org.mapstruct.Mapper
public abstract class WarehouseSiteMapper {

  @Lazy
  @Autowired
  protected WarehouseSiteRepository warehouseSiteRepository;

  public WarehouseSite jpa(WarehouseSiteEntity entity) {
    return WarehouseSite.builder()
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
  public abstract WarehouseSiteEntity jpa(WarehouseSite domain);

  @Mappings({
  })
  public abstract WarehouseSiteMessages.UpdateRequest map(
    WarehouseSiteRequests.UpdateRequest request);

  public abstract WarehouseSiteMessages.DeleteRequest map(
    WarehouseSiteRequests.DeleteRequest request);

  public abstract WarehouseSiteData map(WarehouseSite domain);

  public WarehouseSite map(WarehouseSiteId warehouseSiteId) {
    return Optional.ofNullable(warehouseSiteId)
      .map(id -> warehouseSiteRepository.findBy(id)
        .orElseThrow(WarehouseSiteExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
  })
  public abstract WarehouseSiteMessages.CreateRequest map(
    WarehouseSiteRequests.CreateRequest request);

  public abstract void pass(WarehouseSiteEntity from, @MappingTarget WarehouseSiteEntity to);

}
