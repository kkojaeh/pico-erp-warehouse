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
import pico.erp.warehouse.transaction.order.TransactionOrderMapper;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItem;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemMapper;

@Mapper
public abstract class TransactionOrderItemLotMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  @Autowired
  protected TransactionOrderItemMapper orderItemMapper;

  @Autowired
  protected TransactionOrderMapper orderMapper;

  public TransactionOrderItemLot jpa(TransactionOrderItemLotEntity entity) {
    return TransactionOrderItemLot.builder()
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
  public abstract TransactionOrderItemLotEntity jpa(
    TransactionOrderItemLot domain);

  @Mappings({
    @Mapping(target = "orderItem", source = "orderItemId"),
    @Mapping(target = "itemLot", source = "itemLotId")
  })
  public abstract TransactionOrderItemLotMessages.CreateRequest map(
    TransactionOrderItemLotRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "orderItemId", source = "orderItem.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id")
  })
  public abstract TransactionOrderItemLotData map(
    TransactionOrderItemLot transactionOrderItem);

  @Mappings({
  })
  public abstract TransactionOrderItemLotMessages.UpdateRequest map(
    TransactionOrderItemLotRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract TransactionOrderItemLotMessages.DeleteRequest map(
    TransactionOrderItemLotRequests.DeleteRequest request);

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  protected TransactionOrderItem map(
    TransactionOrderItemId transactionOrderItemId) {
    return orderItemMapper.map(transactionOrderItemId);
  }

  public abstract void pass(
    TransactionOrderItemLotEntity from,
    @MappingTarget TransactionOrderItemLotEntity to);


}
