package pico.erp.warehouse.transaction.order.pack;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.pack.Pack;
import pico.erp.warehouse.pack.PackExceptions;
import pico.erp.warehouse.pack.PackId;
import pico.erp.warehouse.pack.PackRepository;
import pico.erp.warehouse.transaction.order.TransactionOrder;
import pico.erp.warehouse.transaction.order.TransactionOrderId;
import pico.erp.warehouse.transaction.order.TransactionOrderMapper;

@Mapper
public abstract class TransactionOrderPackMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected PackRepository packRepository;

  @Autowired
  protected TransactionOrderMapper orderMapper;

  @Lazy
  @Autowired
  protected TransactionOrderPackRepository orderItemRepository;

  public TransactionOrderPack jpa(TransactionOrderPackEntity entity) {
    return TransactionOrderPack.builder()
      .id(entity.getId())
      .order(map(entity.getOrderId()))
      .pack(map(entity.getPackId()))
      .status(entity.getStatus())
      .build();
  }

  @Mappings({
    @Mapping(target = "orderId", source = "order.id"),
    @Mapping(target = "packId", source = "pack.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract TransactionOrderPackEntity jpa(TransactionOrderPack domain);

  @Mappings({
    @Mapping(target = "order", source = "orderId"),
    @Mapping(target = "pack", source = "packId")
  })
  public abstract TransactionOrderPackMessages.CreateRequest map(
    TransactionOrderPackRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "orderId", source = "order.id"),
    @Mapping(target = "packId", source = "pack.id")
  })
  public abstract TransactionOrderPackData map(
    TransactionOrderPack transactionOrderItem);

  @Mappings({
  })
  public abstract TransactionOrderPackMessages.UpdateRequest map(
    TransactionOrderPackRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract TransactionOrderPackMessages.DeleteRequest map(
    TransactionOrderPackRequests.DeleteRequest request);

  protected Pack map(PackId packId) {
    return Optional.ofNullable(packId)
      .map(id -> packRepository.findBy(id)
        .orElseThrow(PackExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected TransactionOrder map(TransactionOrderId transactionOrderId) {
    return orderMapper.map(transactionOrderId);
  }

  public TransactionOrderPack map(
    TransactionOrderPackId transactionOrderItemId) {
    return Optional.ofNullable(transactionOrderItemId)
      .map(id -> orderItemRepository.findBy(id)
        .orElseThrow(TransactionOrderPackExceptions.NotFoundException::new))
      .orElse(null);
  }

  public abstract void pass(
    TransactionOrderPackEntity from,
    @MappingTarget TransactionOrderPackEntity to);


}
