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
import pico.erp.warehouse.pack.WarehousePack;
import pico.erp.warehouse.pack.WarehousePackExceptions;
import pico.erp.warehouse.pack.WarehousePackId;
import pico.erp.warehouse.pack.WarehousePackRepository;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrder;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderMapper;

@Mapper
public abstract class WarehouseTransactionOrderPackMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected WarehousePackRepository packRepository;

  @Autowired
  protected WarehouseTransactionOrderMapper orderMapper;

  @Lazy
  @Autowired
  protected WarehouseTransactionOrderPackRepository orderItemRepository;

  public WarehouseTransactionOrderPack jpa(WarehouseTransactionOrderPackEntity entity) {
    return WarehouseTransactionOrderPack.builder()
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
  public abstract WarehouseTransactionOrderPackEntity jpa(WarehouseTransactionOrderPack domain);

  @Mappings({
    @Mapping(target = "order", source = "orderId"),
    @Mapping(target = "pack", source = "packId")
  })
  public abstract WarehouseTransactionOrderPackMessages.CreateRequest map(
    WarehouseTransactionOrderPackRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "orderId", source = "order.id"),
    @Mapping(target = "packId", source = "pack.id")
  })
  public abstract WarehouseTransactionOrderPackData map(
    WarehouseTransactionOrderPack transactionOrderItem);

  @Mappings({
  })
  public abstract WarehouseTransactionOrderPackMessages.UpdateRequest map(
    WarehouseTransactionOrderPackRequests.UpdateRequest request);

  @Mappings({
  })
  public abstract WarehouseTransactionOrderPackMessages.DeleteRequest map(
    WarehouseTransactionOrderPackRequests.DeleteRequest request);

  protected WarehousePack map(WarehousePackId packId) {
    return Optional.ofNullable(packId)
      .map(id -> packRepository.findBy(id)
        .orElseThrow(WarehousePackExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected WarehouseTransactionOrder map(WarehouseTransactionOrderId transactionOrderId) {
    return orderMapper.map(transactionOrderId);
  }

  public WarehouseTransactionOrderPack map(
    WarehouseTransactionOrderPackId transactionOrderItemId) {
    return Optional.ofNullable(transactionOrderItemId)
      .map(id -> orderItemRepository.findBy(id)
        .orElseThrow(WarehouseTransactionOrderPackExceptions.NotFoundException::new))
      .orElse(null);
  }

  public abstract void pass(
    WarehouseTransactionOrderPackEntity from,
    @MappingTarget WarehouseTransactionOrderPackEntity to);


}
