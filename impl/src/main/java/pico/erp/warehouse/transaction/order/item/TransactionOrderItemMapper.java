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
import pico.erp.warehouse.transaction.order.TransactionOrder;
import pico.erp.warehouse.transaction.order.TransactionOrderId;
import pico.erp.warehouse.transaction.order.TransactionOrderMapper;

@Mapper
public abstract class TransactionOrderItemMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemService itemService;

  @Autowired
  protected TransactionOrderMapper orderMapper;

  @Lazy
  @Autowired
  protected TransactionOrderItemRepository orderItemRepository;

  public TransactionOrderItem jpa(TransactionOrderItemEntity entity) {
    return TransactionOrderItem.builder()
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
  public abstract TransactionOrderItemEntity jpa(TransactionOrderItem domain);

  @Mappings({
    @Mapping(target = "order", source = "orderId"),
    @Mapping(target = "item", source = "itemId")
  })
  public abstract TransactionOrderItemMessages.CreateRequest map(
    TransactionOrderItemRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "orderId", source = "order.id"),
    @Mapping(target = "itemId", source = "item.id")
  })
  public abstract TransactionOrderItemData map(
    TransactionOrderItem transactionOrderItem);

  @Mappings({
  })
  public abstract TransactionOrderItemMessages.UpdateRequest map(
    TransactionOrderItemRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract TransactionOrderItemMessages.DeleteRequest map(
    TransactionOrderItemRequests.DeleteRequest request);

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected TransactionOrder map(TransactionOrderId transactionOrderId) {
    return orderMapper.map(transactionOrderId);
  }

  public TransactionOrderItem map(
    TransactionOrderItemId transactionOrderItemId) {
    return Optional.ofNullable(transactionOrderItemId)
      .map(id -> orderItemRepository.findBy(id)
        .orElseThrow(TransactionOrderItemExceptions.NotFoundException::new))
      .orElse(null);
  }

  public abstract void pass(
    TransactionOrderItemEntity from,
    @MappingTarget TransactionOrderItemEntity to);


}
