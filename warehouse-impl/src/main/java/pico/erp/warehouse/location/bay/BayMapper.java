package pico.erp.warehouse.location.bay;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.rack.Rack;
import pico.erp.warehouse.location.rack.RackId;
import pico.erp.warehouse.location.rack.RackMapper;

@org.mapstruct.Mapper
public abstract class BayMapper {

  @Autowired
  protected RackMapper rackMapper;

  @Lazy
  @Autowired
  protected BayRepository warehouseBayRepository;

  public Bay jpa(BayEntity entity) {
    return Bay.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .rack(map(entity.getRackId()))
      .deleted(entity.isDeleted())
      .deletedDate(entity.getDeletedDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "rackId", source = "rack.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract BayEntity jpa(Bay domain);

  @Mappings({
  })
  public abstract BayMessages.UpdateRequest map(
    BayRequests.UpdateRequest request);

  public abstract BayMessages.DeleteRequest map(
    BayRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "rackId", source = "rack.id")
  })
  public abstract BayData map(Bay domain);

  public Bay map(BayId warehouseBayId) {
    return Optional.ofNullable(warehouseBayId)
      .map(id -> warehouseBayRepository.findBy(id)
        .orElseThrow(BayExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected Rack map(RackId warehouseRackId) {
    return rackMapper.map(warehouseRackId);
  }

  @Mappings({
    @Mapping(target = "rack", source = "rackId")
  })
  public abstract BayMessages.CreateRequest map(
    BayRequests.CreateRequest request);

  public abstract void pass(BayEntity from, @MappingTarget BayEntity to);

}
