package pico.erp.warehouse.location.bay;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.rack.WarehouseRackMapper;

@org.mapstruct.Mapper
public abstract class WarehouseBayMapper {

  @Autowired
  protected WarehouseRackMapper rackMapper;

  @Lazy
  @Autowired
  protected WarehouseBayRepository warehouseBayRepository;

  public WarehouseBay domain(WarehouseBayEntity entity) {
    return WarehouseBay.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .rack(rackMapper.domain(entity.getRack()))
      .build();
  }

  @Mappings({
    @Mapping(target = "rack", expression = "java(rackMapper.entity(domain.getRack()))"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract WarehouseBayEntity entity(WarehouseBay domain);

  @Mappings({
  })
  public abstract WarehouseBayMessages.UpdateRequest map(
    WarehouseBayRequests.UpdateRequest request);

  public abstract WarehouseBayMessages.DeleteRequest map(
    WarehouseBayRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "rackId", source = "rack.id")
  })
  public abstract WarehouseBayData map(WarehouseBay domain);

  public WarehouseBay map(WarehouseBayId warehouseBayId) {
    return Optional.ofNullable(warehouseBayId)
      .map(id -> warehouseBayRepository.findBy(id)
        .orElseThrow(WarehouseBayExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "rack", expression = "java(rackMapper.map(request.getRackId()))")
  })
  public abstract WarehouseBayMessages.CreateRequest map(
    WarehouseBayRequests.CreateRequest request);

  public abstract void pass(WarehouseBayEntity from, @MappingTarget WarehouseBayEntity to);

}
