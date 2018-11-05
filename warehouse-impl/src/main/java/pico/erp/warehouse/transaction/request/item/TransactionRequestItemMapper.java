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
import pico.erp.warehouse.transaction.request.TransactionRequest;
import pico.erp.warehouse.transaction.request.TransactionRequestId;
import pico.erp.warehouse.transaction.request.TransactionRequestMapper;

@Mapper
public abstract class TransactionRequestItemMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemService itemService;

  @Autowired
  protected TransactionRequestMapper requestMapper;

  @Lazy
  @Autowired
  protected TransactionRequestItemRepository requestItemRepository;

  public TransactionRequestItem jpa(TransactionRequestItemEntity entity) {
    return TransactionRequestItem.builder()
      .id(entity.getId())
      .request(map(entity.getRequestId()))
      .item(map(entity.getItemId()))
      .quantity(entity.getQuantity())
      .build();
  }

  @Mappings({
    @Mapping(target = "requestId", source = "request.id"),
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract TransactionRequestItemEntity jpa(TransactionRequestItem domain);

  @Mappings({
    @Mapping(target = "request", source = "requestId"),
    @Mapping(target = "item", source = "itemId")
  })
  public abstract TransactionRequestItemMessages.CreateRequest map(
    TransactionRequestItemRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "requestId", source = "request.id"),
    @Mapping(target = "itemId", source = "item.id")
  })
  public abstract TransactionRequestItemData map(
    TransactionRequestItem requestItem);

  @Mappings({
  })
  public abstract TransactionRequestItemMessages.UpdateRequest map(
    TransactionRequestItemRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract TransactionRequestItemMessages.DeleteRequest map(
    TransactionRequestItemRequests.DeleteRequest request);

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected TransactionRequest map(TransactionRequestId requestId) {
    return requestMapper.map(requestId);
  }

  public TransactionRequestItem map(
    TransactionRequestItemId requestItemId) {
    return Optional.ofNullable(requestItemId)
      .map(id -> requestItemRepository.findBy(id)
        .orElseThrow(TransactionRequestItemExceptions.NotFoundException::new))
      .orElse(null);
  }

  public abstract void pass(
    TransactionRequestItemEntity from,
    @MappingTarget TransactionRequestItemEntity to);


}
