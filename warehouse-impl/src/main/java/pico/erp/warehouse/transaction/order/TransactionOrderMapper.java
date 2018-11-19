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
import pico.erp.warehouse.location.station.Station;
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.location.station.StationMapper;
import pico.erp.warehouse.pack.PackSelector;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemRepository;
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotRepository;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackRepository;

@Mapper
public abstract class TransactionOrderMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Autowired
  protected StationMapper stationMapper;

  @Lazy
  @Autowired
  protected TransactionOrderRepository orderRepository;

  @Lazy
  @Autowired
  protected TransactionOrderItemRepository orderItemRepository;

  @Autowired
  private TransactionOrderItemLotRepository orderItemLotRepository;

  @Autowired
  private TransactionOrderPackRepository orderPackRepository;

  @Autowired
  protected PackSelector packSelector;

  @Autowired
  protected TransactionOrderCodeGenerator transactionOrderCodeGenerator;

  public TransactionOrderAggregator aggregator(
    TransactionOrderEntity entity) {
    return TransactionOrderAggregator.aggregatorBuilder()
      .id(entity.getId())
      .code(entity.getCode())
      .dueDate(entity.getDueDate())
      .relatedCompany(map(entity.getTransactionCompanyId()))
      .station(map(entity.getStationId()))
      .committedBy(entity.getCommittedBy())
      .committedDate(entity.getCommittedDate())
      .canceledBy(entity.getCanceledBy())
      .canceledDate(entity.getCanceledDate())
      .acceptedBy(entity.getAcceptedBy())
      .acceptedDate(entity.getAcceptedDate())
      .completedBy(entity.getCompletedBy())
      .completedDate(entity.getCompletedDate())
      .status(entity.getStatus())
      .type(entity.getType())
      .committable(entity.isCommittable())
      .quantityCorrectionPolicy(entity.getQuantityCorrectionPolicy())
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

  public TransactionOrder jpa(TransactionOrderEntity entity) {
    return TransactionOrder.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .dueDate(entity.getDueDate())
      .transactionCompany(map(entity.getTransactionCompanyId()))
      .station(map(entity.getStationId()))
      .committedBy(entity.getCommittedBy())
      .committedDate(entity.getCommittedDate())
      .canceledBy(entity.getCanceledBy())
      .canceledDate(entity.getCanceledDate())
      .acceptedBy(entity.getAcceptedBy())
      .acceptedDate(entity.getAcceptedDate())
      .completedBy(entity.getCompletedBy())
      .completedDate(entity.getCompletedDate())
      .status(entity.getStatus())
      .type(entity.getType())
      .committable(entity.isCommittable())
      .quantityCorrectionPolicy(entity.getQuantityCorrectionPolicy())
      .build();
  }

  @Mappings({
    @Mapping(target = "stationId", source = "station.id"),
    @Mapping(target = "requestId", source = "request.id"),
    @Mapping(target = "transactionCompanyId", source = "transactionCompany.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract TransactionOrderEntity jpa(TransactionOrder domain);

  @Mappings({
    @Mapping(target = "transactionCompany", source = "transactionCompanyId"),
    @Mapping(target = "station", source = "stationId"),
    @Mapping(target = "codeGenerator", expression = "java(transactionOrderCodeGenerator)")
  })
  public abstract TransactionOrderMessages.CreateRequest map(
    TransactionOrderRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "relatedCompanyId", source = "transactionCompany.id"),
    @Mapping(target = "stationId", source = "station.id"),
    @Mapping(target = "requestId", source = "request.id")
  })
  public abstract TransactionOrderData map(
    TransactionOrder transactionOrder);

  @Mappings({
    @Mapping(target = "transactionCompany", source = "transactionCompanyId"),
    @Mapping(target = "station", source = "stationId")
  })
  public abstract TransactionOrderMessages.UpdateRequest map(
    TransactionOrderRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "committedBy", expression = "java(auditorAware.getCurrentAuditor())"),
    @Mapping(target = "packSelector", expression = "java(packSelector)")
  })
  public abstract TransactionOrderMessages.CommitRequest map(
    TransactionOrderRequests.CommitRequest request);

  @Mappings({
    @Mapping(target = "canceledBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract TransactionOrderMessages.CancelRequest map(
    TransactionOrderRequests.CancelRequest request);

  @Mappings({
    @Mapping(target = "acceptedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract TransactionOrderMessages.AcceptRequest map(
    TransactionOrderRequests.AcceptRequest request);

  @Mappings({
    @Mapping(target = "completedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract TransactionOrderMessages.CompleteRequest map(
    TransactionOrderRequests.CompleteRequest request);

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  protected Station map(StationId warehouseStationId) {
    return stationMapper.map(warehouseStationId);
  }

  public TransactionOrder map(TransactionOrderId transactionOrderId) {
    return Optional.ofNullable(transactionOrderId)
      .map(id -> orderRepository.findBy(id)
        .orElseThrow(TransactionOrderExceptions.NotFoundException::new))
      .orElse(null);
  }

  public abstract void pass(
    TransactionOrderEntity from, @MappingTarget TransactionOrderEntity to);


}
