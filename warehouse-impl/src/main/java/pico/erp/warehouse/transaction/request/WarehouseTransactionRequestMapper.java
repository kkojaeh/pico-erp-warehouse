package pico.erp.warehouse.transaction.request;

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
import pico.erp.warehouse.location.station.WarehouseStationEntity;
import pico.erp.warehouse.location.station.WarehouseStationId;
import pico.erp.warehouse.location.station.WarehouseStationMapper;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemRepository;
import pico.erp.warehouse.transaction.request.item.lot.WarehouseTransactionRequestItemLotRepository;

@Mapper
public abstract class WarehouseTransactionRequestMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Autowired
  protected WarehouseStationMapper stationMapper;

  @Lazy
  @Autowired
  protected WarehouseTransactionRequestRepository transactionRequestRepository;

  @Lazy
  @Autowired
  protected WarehouseTransactionRequestItemRepository transactionRequestItemRepository;

  @Autowired
  private WarehouseTransactionRequestItemLotRepository warehouseTransactionRequestItemLotRepository;

  public WarehouseTransactionRequestAggregator aggregator(
    WarehouseTransactionRequestEntity entity) {
    return WarehouseTransactionRequestAggregator.aggregatorBuilder()
      .id(entity.getId())
      .dueDate(entity.getDueDate())
      .relatedCompany(map(entity.getRelatedCompanyId()))
      .station(jpa(entity.getStation()))
      .committedBy(entity.getCommittedBy())
      .committedDate(entity.getCommittedDate())
      .canceledBy(entity.getCanceledBy())
      .canceledDate(entity.getCanceledDate())
      .status(entity.getStatus())
      .type(entity.getType())
      .committable(entity.isCommittable())
      .items(
        transactionRequestItemRepository.findAllBy(entity.getId()).collect(Collectors.toList())
      )
      .itemLots(
        warehouseTransactionRequestItemLotRepository.findAllBy(entity.getId())
          .collect(Collectors.toList())
      )
      .build();
  }

  public WarehouseTransactionRequest jpa(WarehouseTransactionRequestEntity entity) {
    return WarehouseTransactionRequest.builder()
      .id(entity.getId())
      .dueDate(entity.getDueDate())
      .relatedCompany(map(entity.getRelatedCompanyId()))
      .station(jpa(entity.getStation()))
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
    @Mapping(target = "relatedCompanyId", source = "relatedCompany.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract WarehouseTransactionRequestEntity jpa(WarehouseTransactionRequest domain);

  protected WarehouseStation jpa(WarehouseStationEntity entity) {
    return stationMapper.jpa(entity);
  }

  protected WarehouseStationEntity jpa(WarehouseStation domain) {
    return stationMapper.jpa(domain);
  }

  @Mappings({
    @Mapping(target = "relatedCompany", source = "relatedCompanyId"),
    @Mapping(target = "station", source = "stationId")
  })
  public abstract WarehouseTransactionRequestMessages.CreateRequest map(
    WarehouseTransactionRequestRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "relatedCompanyId", source = "relatedCompany.id"),
    @Mapping(target = "stationId", source = "station.id")
  })
  public abstract WarehouseTransactionRequestData map(
    WarehouseTransactionRequest transactionRequest);

  @Mappings({
    @Mapping(target = "relatedCompany", source = "relatedCompanyId"),
    @Mapping(target = "station", source = "stationId")
  })
  public abstract WarehouseTransactionRequestMessages.UpdateRequest map(
    WarehouseTransactionRequestRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "committedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionRequestMessages.CommitRequest map(
    WarehouseTransactionRequestRequests.CommitRequest request);

  @Mappings({
    @Mapping(target = "canceledBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionRequestMessages.CancelRequest map(
    WarehouseTransactionRequestRequests.CancelRequest request);

  @Mappings({
    @Mapping(target = "acceptedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionRequestMessages.AcceptRequest map(
    WarehouseTransactionRequestRequests.AcceptRequest request);

  @Mappings({
    @Mapping(target = "completedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract WarehouseTransactionRequestMessages.CompleteRequest map(
    WarehouseTransactionRequestRequests.CompleteRequest request);

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  protected WarehouseStation map(WarehouseStationId warehouseStationId) {
    return stationMapper.map(warehouseStationId);
  }

  public abstract void pass(
    WarehouseTransactionRequestEntity from, @MappingTarget WarehouseTransactionRequestEntity to);

  public WarehouseTransactionRequest map(WarehouseTransactionRequestId transactionRequestId) {
    return Optional.ofNullable(transactionRequestId)
      .map(id -> transactionRequestRepository.findBy(id)
        .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new))
      .orElse(null);
  }


}
