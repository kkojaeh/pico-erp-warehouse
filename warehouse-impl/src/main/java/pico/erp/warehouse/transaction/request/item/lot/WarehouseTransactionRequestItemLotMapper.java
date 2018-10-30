package pico.erp.warehouse.transaction.request.item.lot;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.item.lot.ItemLotData;
import pico.erp.item.lot.ItemLotId;
import pico.erp.item.lot.ItemLotService;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestMapper;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItem;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemMapper;

@Mapper
public abstract class WarehouseTransactionRequestItemLotMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  @Autowired
  protected WarehouseTransactionRequestItemMapper transactionRequestItemMapper;

  @Autowired
  protected WarehouseTransactionRequestMapper transactionRequestMapper;

  public WarehouseTransactionRequestItemLot jpa(WarehouseTransactionRequestItemLotEntity entity) {
    return WarehouseTransactionRequestItemLot.builder()
      .id(entity.getId())
      .transactionRequestItem(map(entity.getTransactionRequestItemId()))
      .itemLot(map(entity.getItemLotId()))
      .quantity(entity.getQuantity())
      .build();
  }

  @Mappings({
    @Mapping(target = "itemLotId", source = "itemLot.id"),
    @Mapping(target = "transactionRequestItemId", source = "transactionRequestItem.id"),
    @Mapping(target = "transactionRequestId", source = "transactionRequestItem.transactionRequest.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract WarehouseTransactionRequestItemLotEntity jpa(
    WarehouseTransactionRequestItemLot domain);

  @Mappings({
    @Mapping(target = "transactionRequestItem", source = "transactionRequestItemId"),
    @Mapping(target = "itemLot", source = "itemLotId")
  })
  public abstract WarehouseTransactionRequestItemLotMessages.CreateRequest map(
    WarehouseTransactionRequestItemLotRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "transactionRequestItemId", source = "transactionRequestItem.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id")
  })
  public abstract WarehouseTransactionRequestItemLotData map(
    WarehouseTransactionRequestItemLot transactionRequestItem);

  @Mappings({
  })
  public abstract WarehouseTransactionRequestItemLotMessages.UpdateRequest map(
    WarehouseTransactionRequestItemLotRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract WarehouseTransactionRequestItemLotMessages.DeleteRequest map(
    WarehouseTransactionRequestItemLotRequests.DeleteRequest request);

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  protected WarehouseTransactionRequestItem map(
    WarehouseTransactionRequestItemId transactionRequestItemId) {
    return transactionRequestItemMapper.map(transactionRequestItemId);
  }

  public abstract void pass(
    WarehouseTransactionRequestItemLotEntity from,
    @MappingTarget WarehouseTransactionRequestItemLotEntity to);


}
