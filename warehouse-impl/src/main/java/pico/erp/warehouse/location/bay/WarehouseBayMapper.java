package pico.erp.warehouse.location.bay;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.warehouse.location.rack.WarehouseRack;
import pico.erp.warehouse.location.rack.WarehouseRackEntity;
import pico.erp.warehouse.location.rack.WarehouseRackId;
import pico.erp.warehouse.location.rack.WarehouseRackMapper;

@org.mapstruct.Mapper
public abstract class WarehouseBayMapper {

  @Autowired
  protected WarehouseRackMapper rackMapper;

  @Lazy
  @Autowired
  protected WarehouseBayRepository warehouseBayRepository;

  public WarehouseBay jpa(WarehouseBayEntity entity) {
    return WarehouseBay.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .locationCode(entity.getLocationCode())
      .rack(rackMapper.jpa(entity.getRack()))
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
  public abstract WarehouseBayEntity jpa(WarehouseBay domain);

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

  protected WarehouseRackEntity jpa(WarehouseRack domain) {
    return rackMapper.jpa(domain);
  }

  public abstract void pass(WarehouseBayEntity from, @MappingTarget WarehouseBayEntity to);

  protected WarehouseRack map(WarehouseRackId warehouseRackId) {
    return rackMapper.map(warehouseRackId);
  }

  @Mappings({
    @Mapping(target = "rack", source = "rackId")
  })
  public abstract WarehouseBayMessages.CreateRequest map(
    WarehouseBayRequests.CreateRequest request);

}
