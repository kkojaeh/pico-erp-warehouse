package pico.erp.warehouse.transaction.order.item.lot;

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
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderMapper;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItem;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemMapper;

@Mapper
public abstract class WarehouseTransactionOrderItemLotMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  @Autowired
  protected WarehouseTransactionOrderItemMapper transactionOrderItemMapper;

  @Autowired
  protected WarehouseTransactionOrderMapper transactionOrderMapper;

  public WarehouseTransactionOrderItemLot jpa(WarehouseTransactionOrderItemLotEntity entity) {
    return WarehouseTransactionOrderItemLot.builder()
      .id(entity.getId())
      .orderItem(map(entity.getOrderItemId()))
      .itemLot(map(entity.getItemLotId()))
      .quantity(entity.getQuantity())
      .build();
  }

  @Mappings({
    @Mapping(target = "itemLotId", source = "itemLot.id"),
    @Mapping(target = "orderItemId", source = "orderItem.id"),
    @Mapping(target = "orderId", source = "orderItem.order.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract WarehouseTransactionOrderItemLotEntity jpa(
    WarehouseTransactionOrderItemLot domain);

  @Mappings({
    @Mapping(target = "orderItem", source = "orderItemId"),
    @Mapping(target = "itemLot", source = "itemLotId")
  })
  public abstract WarehouseTransactionOrderItemLotMessages.CreateRequest map(
    WarehouseTransactionOrderItemLotRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "orderItemId", source = "orderItem.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id")
  })
  public abstract WarehouseTransactionOrderItemLotData map(
    WarehouseTransactionOrderItemLot transactionOrderItem);

  @Mappings({
  })
  public abstract WarehouseTransactionOrderItemLotMessages.UpdateRequest map(
    WarehouseTransactionOrderItemLotRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract WarehouseTransactionOrderItemLotMessages.DeleteRequest map(
    WarehouseTransactionOrderItemLotRequests.DeleteRequest request);

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  protected WarehouseTransactionOrderItem map(
    WarehouseTransactionOrderItemId transactionOrderItemId) {
    return transactionOrderItemMapper.map(transactionOrderItemId);
  }

  public abstract void pass(
    WarehouseTransactionOrderItemLotEntity from,
    @MappingTarget WarehouseTransactionOrderItemLotEntity to);


}
