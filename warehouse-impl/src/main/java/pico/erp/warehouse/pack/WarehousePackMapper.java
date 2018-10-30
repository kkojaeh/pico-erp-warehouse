package pico.erp.warehouse.pack;

import java.util.Optional;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.item.lot.ItemLotData;
import pico.erp.item.lot.ItemLotId;
import pico.erp.item.lot.ItemLotService;
import pico.erp.warehouse.location.WarehouseLocation;
import pico.erp.warehouse.location.WarehouseLocationId;
import pico.erp.warehouse.location.WarehouseLocationMapper;
import pico.erp.warehouse.pack.code.WarehousePackCodeGenerator;

@org.mapstruct.Mapper
public abstract class WarehousePackMapper {

  @Autowired
  protected WarehousePackCodeGenerator codeGenerator;

  @Lazy
  @Autowired
  protected WarehouseLocationMapper locationMapper;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  public WarehousePack jpa(WarehousePackEntity entity) {
    return WarehousePack.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .location(
        map(entity.getLocationId())
      )
      .itemLot(
        map(entity.getItemLotId())
      )
      .quantity(entity.getQuantity())
      .status(entity.getStatus())
      .createdBy(entity.getCreatedBy())
      .createdDate(entity.getCreatedDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "locationId", source = "location.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id"),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract WarehousePackEntity jpa(WarehousePack domain);

  @Mappings({
    @Mapping(target = "locationId", source = "location.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id")
  })
  public abstract WarehousePackData map(WarehousePack pack);

  @Mappings({
    @Mapping(target = "codeGenerator", expression = "java(codeGenerator)")
  })
  public abstract WarehousePackMessages.CreateRequest map(
    WarehousePackRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "itemLot", source = "itemLotId")
  })
  public abstract WarehousePackMessages.PackRequest map(
    WarehousePackRequests.PackRequest request);

  @Mappings({
    @Mapping(target = "location", source = "locationId")
  })
  public abstract WarehousePackMessages.PutRequest map(
    WarehousePackRequests.PutRequest request);

  public abstract WarehousePackMessages.DeleteRequest map(
    WarehousePackRequests.DeleteRequest request);

  protected WarehouseLocation map(WarehouseLocationId locationId) {
    return locationMapper.map(locationId);
  }

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  public abstract void pass(WarehousePackEntity from, @MappingTarget WarehousePackEntity to);


}
