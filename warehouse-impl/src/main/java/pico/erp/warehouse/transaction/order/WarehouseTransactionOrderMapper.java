package pico.erp.warehouse.transaction.order;

import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.company.CompanyData;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStation;
import pico.erp.warehouse.location.station.WarehouseStationId;
import pico.erp.warehouse.location.station.WarehouseStationMapper;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemRepository;
import pico.erp.warehouse.transaction.order.item.lot.WarehouseTransactionOrderItemLotRepository;
import pico.erp.warehouse.transaction.order.pack.WarehouseTransactionOrderPackRepository;

@Mapper
public abstract class WarehouseTransactionOrderMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Autowired
  protected WarehouseStationMapper stationMapper;

  @Lazy
  @Autowired
  protected WarehouseTransactionOrderRepository orderRepository;

  @Lazy
  @Autowired
  protected WarehouseTransactionOrderItemRepository orderItemRepository;

  @Autowired
  private WarehouseTransactionOrderItemLotRepository orderItemLotRepository;

  @Autowired
  private WarehouseTransactionOrderPackRepository orderPackRepository;

  public WarehouseTransactionOrderAggregator aggregator(
    WarehouseTransactionOrderEntity entity) {
    return WarehouseTransactionOrderAggregator.aggregatorBuilder()
      .id(entity.getId())
      .dueDate(entity.getDueDate())
      .relatedCompany(map(entity.getRelatedCompanyId()))
      .station(map(entity.getStationId()))
      .committedBy(entity.getCommittedBy())
      .committedDate(entity.getCommittedDate())
      .canceledBy(entity.getCanceledBy())
      .canceledDate(entity.getCanceledDate())
      .status(entity.getStatus())
      .type(entity.getType())
      .committable(entity.isCommittable())
      .items(
        orderItemRepository.findAllBy(entity.getId()).collect(Collectors.toList())
      )
      .itemLots(
        orderItemLotRepository.findAllBy(entity.getId())
          .collect(Collectors.toList())
      )
      .packs(
        orderPackRepository.findAllBy(entity.getId()).collect(Collectors.toList())
      )
      .build();
  }

  public WarehouseTransactionOrder jpa(WarehouseTransactionOrderEntity entity) {
    return WarehouseTransactionOrder.builder()
      .id(entity.getId())
      .dueDate(entity.getDueDate())
      .relatedCompany(map(entity.getRelatedCompanyId()))
      .station(map(entity.getStationId()))
      .committedBy(entity.getCommittedBy())
      .committedDate(entity.getCommittedDate())
      .canceledBy(entity.getCanceledBy())
      .canceledDate(entity.getCanceledDate())
      .status(entity.getStatus())
      .type(entity.getType())
      .committable(entity.isCommittable())
      .build();
  }

  @Mappings({
    @Mapping(target = "stationId", source = "station.id"),
    @Mapping(target = "requestId", source = "request.id"),
    @Mapping(target = "relatedCompanyId", source = "relatedCompany.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract WarehouseTransactionOrderEntity jpa(WarehouseTransactionOrder domain);

  @Mappings({
    @Mapping(target = "relatedCompany", source = "relatedCompanyId"),
    @Mapping(target = "station", source = "stationId")
  })
  public abstract WarehouseTransactionOrderMessages.CreateRequest map(
    WarehouseTransactionOrderRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "relatedCompanyId", source = "relatedCompany.id"),
    @Mapping(target = "stationId", source = "station.id"),
    @Mapping(target = "requestId", source = "request.id")
  })
  public abstract WarehouseTransactionOrderData map(
    WarehouseTransactionOrder transactionOrder);

  @Mappings({
    @Mapping(target = "relatedCompany", source = "relatedCompanyId"),
    @Mapping(target = "station", source = "stationId")
  })
  public abstract WarehouseTransactionOrderMessages.UpdateRequest map(
    WarehouseTransactionOrderRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "committedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionOrderMessages.CommitRequest map(
    WarehouseTransactionOrderRequests.CommitRequest request);

  @Mappings({
    @Mapping(target = "canceledBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionOrderMessages.CancelRequest map(
    WarehouseTransactionOrderRequests.CancelRequest request);

  @Mappings({
    @Mapping(target = "acceptedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionOrderMessages.AcceptRequest map(
    WarehouseTransactionOrderRequests.AcceptRequest request);

  @Mappings({
    @Mapping(target = "completedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionOrderMessages.CompleteRequest map(
    WarehouseTransactionOrderRequests.CompleteRequest request);

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  protected WarehouseStation map(WarehouseStationId warehouseStationId) {
    return stationMapper.map(warehouseStationId);
  }

  public WarehouseTransactionOrder map(WarehouseTransactionOrderId transactionOrderId) {
    return Optional.ofNullable(transactionOrderId)
      .map(id -> orderRepository.findBy(id)
        .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new))
      .orElse(null);
  }

  public abstract void pass(
    WarehouseTransactionOrderEntity from, @MappingTarget WarehouseTransactionOrderEntity to);


}
