package pico.erp.warehouse.location.rack;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.zone.Zone;
import pico.erp.warehouse.location.zone.ZoneId;
import pico.erp.warehouse.location.zone.ZoneMapper;

@org.mapstruct.Mapper
public abstract class RackMapper {

  @Autowired
  protected ZoneMapper zoneMapper;

  @Lazy
  @Autowired
  protected RackRepository warehouseRackRepository;

  public Rack jpa(RackEntity entity) {
    return Rack.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .zone(map(entity.getZoneId()))
      .deleted(entity.isDeleted())
      .deletedDate(entity.getDeletedDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "zoneId", source = "zone.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract RackEntity jpa(Rack domain);


  @Mappings({
  })
  public abstract RackMessages.UpdateRequest map(
    RackRequests.UpdateRequest request);

  public abstract RackMessages.DeleteRequest map(
    RackRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "zoneId", source = "zone.id")
  })
  public abstract RackData map(Rack domain);

  public Rack map(RackId warehouseRackId) {
    return Optional.ofNullable(warehouseRackId)
      .map(id -> warehouseRackRepository.findBy(id)
        .orElseThrow(RackExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected Zone map(ZoneId warehouseZoneId) {
    return zoneMapper.map(warehouseZoneId);
  }

  @Mappings({
    @Mapping(target = "zone", source = "zoneId")
  })
  public abstract RackMessages.CreateRequest map(
    RackRequests.CreateRequest request);

  public abstract void pass(RackEntity from, @MappingTarget RackEntity to);

}
