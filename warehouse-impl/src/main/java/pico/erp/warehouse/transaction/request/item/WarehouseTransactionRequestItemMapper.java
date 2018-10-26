package pico.erp.warehouse.transaction.request.item;

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
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequest;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestMapper;

@Mapper
public abstract class WarehouseTransactionRequestItemMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemService itemService;

  @Autowired
  protected WarehouseTransactionRequestMapper transactionRequestMapper;

  @Lazy
  @Autowired
  protected WarehouseTransactionRequestItemRepository transactionRequestItemRepository;

  public WarehouseTransactionRequestItem jpa(WarehouseTransactionRequestItemEntity entity) {
    return WarehouseTransactionRequestItem.builder()
      .id(entity.getId())
      .transactionRequest(map(entity.getTransactionRequestId()))
      .item(map(entity.getItemId()))
      .quantity(entity.getQuantity())
      .build();
  }

  @Mappings({
    @Mapping(target = "transactionRequestId", source = "transactionRequest.id"),
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract WarehouseTransactionRequestItemEntity jpa(WarehouseTransactionRequestItem domain);

  @Mappings({
    @Mapping(target = "transactionRequest", source = "transactionRequestId"),
    @Mapping(target = "item", source = "itemId")
  })
  public abstract WarehouseTransactionRequestItemMessages.CreateRequest map(
    WarehouseTransactionRequestItemRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "transactionRequestId", source = "transactionRequest.id"),
    @Mapping(target = "itemId", source = "item.id")
  })
  public abstract WarehouseTransactionRequestItemData map(
    WarehouseTransactionRequestItem transactionRequestItem);

  @Mappings({
  })
  public abstract WarehouseTransactionRequestItemMessages.UpdateRequest map(
    WarehouseTransactionRequestItemRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract WarehouseTransactionRequestItemMessages.DeleteRequest map(
    WarehouseTransactionRequestItemRequests.DeleteRequest request);

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected WarehouseTransactionRequest map(WarehouseTransactionRequestId transactionRequestId) {
    return transactionRequestMapper.map(transactionRequestId);
  }

  public abstract void pass(
    WarehouseTransactionRequestItemEntity from,
    @MappingTarget WarehouseTransactionRequestItemEntity to);

  public WarehouseTransactionRequestItem map(
    WarehouseTransactionRequestItemId transactionRequestItemId) {
    return Optional.ofNullable(transactionRequestItemId)
      .map(id -> transactionRequestItemRepository.findBy(id)
        .orElseThrow(WarehouseTransactionRequestItemExceptions.NotFoundException::new))
      .orElse(null);
  }


}
