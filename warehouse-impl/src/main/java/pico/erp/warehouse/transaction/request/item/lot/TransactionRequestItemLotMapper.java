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
import pico.erp.warehouse.transaction.request.TransactionRequestMapper;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItem;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemMapper;

@Mapper
public abstract class TransactionRequestItemLotMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  @Autowired
  protected TransactionRequestItemMapper requestItemMapper;

  @Autowired
  protected TransactionRequestMapper requestMapper;

  public TransactionRequestItemLot jpa(TransactionRequestItemLotEntity entity) {
    return TransactionRequestItemLot.builder()
      .id(entity.getId())
      .requestItem(map(entity.getRequestItemId()))
      .itemLot(map(entity.getItemLotId()))
      .quantity(entity.getQuantity())
      .build();
  }

  @Mappings({
    @Mapping(target = "itemLotId", source = "itemLot.id"),
    @Mapping(target = "requestItemId", source = "requestItem.id"),
    @Mapping(target = "requestId", source = "requestItem.request.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract TransactionRequestItemLotEntity jpa(
    TransactionRequestItemLot domain);

  @Mappings({
    @Mapping(target = "requestItem", source = "requestItemId"),
    @Mapping(target = "itemLot", source = "itemLotId")
  })
  public abstract TransactionRequestItemLotMessages.CreateRequest map(
    TransactionRequestItemLotRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "requestItemId", source = "requestItem.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id")
  })
  public abstract TransactionRequestItemLotData map(
    TransactionRequestItemLot requestItem);

  @Mappings({
  })
  public abstract TransactionRequestItemLotMessages.UpdateRequest map(
    TransactionRequestItemLotRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract TransactionRequestItemLotMessages.DeleteRequest map(
    TransactionRequestItemLotRequests.DeleteRequest request);

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  protected TransactionRequestItem map(
    TransactionRequestItemId requestItemId) {
    return requestItemMapper.map(requestItemId);
  }

  public abstract void pass(
    TransactionRequestItemLotEntity from,
    @MappingTarget TransactionRequestItemLotEntity to);


}
