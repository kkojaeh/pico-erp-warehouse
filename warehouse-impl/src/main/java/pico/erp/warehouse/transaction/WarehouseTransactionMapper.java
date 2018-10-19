package pico.erp.warehouse.transaction;

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
import pico.erp.warehouse.pack.code.WarehousePackCodeGenerator;

@Mapper
public abstract class WarehouseTransactionMapper {

  @Autowired
  protected WarehousePackCodeGenerator codeGenerator;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  public WarehouseTransaction jpa(WarehouseTransactionEntity entity) {
    return WarehouseTransaction.builder()
      .id(entity.getId())
      .itemLot(map(entity.getItemLotId()))
      .quantity(entity.getQuantity())
      .transactedBy(entity.getTransactedBy())
      .transactedDate(entity.getTransactedDate())
      .quantity(entity.getQuantity())
      .type(entity.getType())
      .build();
  }

  @Mappings({
    @Mapping(target = "itemLotId", source = "itemLot.id"),
    @Mapping(target = "itemId", source = "itemLot.itemId")
  })
  public abstract WarehouseTransactionEntity jpa(WarehouseTransaction domain);

  @Mappings({
    @Mapping(target = "itemLot", source = "itemLotId"),
    @Mapping(target = "transactedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionMessages.OutboundRequest map(
    WarehouseTransactionRequests.OutboundRequest request);

  @Mappings({
    @Mapping(target = "itemLotId", source = "itemLot.id")
  })
  public abstract WarehouseTransactionData map(WarehouseTransaction transaction);

  @Mappings({
    @Mapping(target = "itemLot", source = "itemLotId"),
    @Mapping(target = "transactedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionMessages.InboundRequest map(
    WarehouseTransactionRequests.InboundRequest request);

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  public abstract void pass(
    WarehouseTransactionEntity from, @MappingTarget WarehouseTransactionEntity to);


}
