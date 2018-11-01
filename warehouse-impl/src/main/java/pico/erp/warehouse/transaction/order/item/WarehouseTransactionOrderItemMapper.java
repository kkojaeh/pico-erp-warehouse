package pico.erp.warehouse.transaction.order.item;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrder;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderMapper;

@Mapper
public abstract class WarehouseTransactionOrderItemMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemService itemService;

  @Autowired
  protected WarehouseTransactionOrderMapper transactionOrderMapper;

  @Lazy
  @Autowired
  protected WarehouseTransactionOrderItemRepository transactionOrderItemRepository;

  public WarehouseTransactionOrderItem jpa(WarehouseTransactionOrderItemEntity entity) {
    return WarehouseTransactionOrderItem.builder()
      .id(entity.getId())
      .order(map(entity.getOrderId()))
      .item(map(entity.getItemId()))
      .quantity(entity.getQuantity())
      .build();
  }

  @Mappings({
    @Mapping(target = "orderId", source = "order.id"),
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract WarehouseTransactionOrderItemEntity jpa(WarehouseTransactionOrderItem domain);

  @Mappings({
    @Mapping(target = "order", source = "orderId"),
    @Mapping(target = "item", source = "itemId")
  })
  public abstract WarehouseTransactionOrderItemMessages.CreateRequest map(
    WarehouseTransactionOrderItemRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "orderId", source = "order.id"),
    @Mapping(target = "itemId", source = "item.id")
  })
  public abstract WarehouseTransactionOrderItemData map(
    WarehouseTransactionOrderItem transactionOrderItem);

  @Mappings({
  })
  public abstract WarehouseTransactionOrderItemMessages.UpdateRequest map(
    WarehouseTransactionOrderItemRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract WarehouseTransactionOrderItemMessages.DeleteRequest map(
    WarehouseTransactionOrderItemRequests.DeleteRequest request);

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected WarehouseTransactionOrder map(WarehouseTransactionOrderId transactionOrderId) {
    return transactionOrderMapper.map(transactionOrderId);
  }

  public WarehouseTransactionOrderItem map(
    WarehouseTransactionOrderItemId transactionOrderItemId) {
    return Optional.ofNullable(transactionOrderItemId)
      .map(id -> transactionOrderItemRepository.findBy(id)
        .orElseThrow(WarehouseTransactionOrderItemExceptions.NotFoundException::new))
      .orElse(null);
  }

  public abstract void pass(
    WarehouseTransactionOrderItemEntity from,
    @MappingTarget WarehouseTransactionOrderItemEntity to);


}
