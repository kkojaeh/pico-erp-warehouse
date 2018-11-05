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
import pico.erp.warehouse.location.Location;
import pico.erp.warehouse.location.LocationId;
import pico.erp.warehouse.location.LocationMapper;
import pico.erp.warehouse.pack.code.PackCodeGenerator;

@org.mapstruct.Mapper
public abstract class PackMapper {

  @Autowired
  protected PackCodeGenerator codeGenerator;

  @Lazy
  @Autowired
  protected LocationMapper locationMapper;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  public Pack jpa(PackEntity entity) {
    return Pack.builder()
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
    @Mapping(target = "itemId", source = "itemLot.itemId"),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract PackEntity jpa(Pack domain);

  @Mappings({
    @Mapping(target = "locationId", source = "location.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id")
  })
  public abstract PackData map(Pack pack);

  @Mappings({
    @Mapping(target = "codeGenerator", expression = "java(codeGenerator)")
  })
  public abstract PackMessages.CreateRequest map(
    PackRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "itemLot", source = "itemLotId")
  })
  public abstract PackMessages.PackRequest map(
    PackRequests.PackRequest request);

  @Mappings({
    @Mapping(target = "location", source = "locationId")
  })
  public abstract PackMessages.PutRequest map(
    PackRequests.PutRequest request);

  public abstract PackMessages.DeleteRequest map(
    PackRequests.DeleteRequest request);

  protected Location map(LocationId locationId) {
    return locationMapper.map(locationId);
  }

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  public abstract void pass(PackEntity from, @MappingTarget PackEntity to);


}
