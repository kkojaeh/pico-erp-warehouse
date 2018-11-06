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
import pico.erp.warehouse.location.station.Station;
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.location.station.StationMapper;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemRepository;
import pico.erp.warehouse.transaction.request.item.lot.TransactionRequestItemLotRepository;

@Mapper
public abstract class TransactionRequestMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Autowired
  protected StationMapper stationMapper;

  @Lazy
  @Autowired
  protected TransactionRequestRepository requestRepository;

  @Lazy
  @Autowired
  protected TransactionRequestItemRepository requestItemRepository;

  @Autowired
  private TransactionRequestItemLotRepository requestItemLotRepository;

  public TransactionRequestAggregator aggregator(
    TransactionRequestEntity entity) {
    return TransactionRequestAggregator.aggregatorBuilder()
      .id(entity.getId())
      .dueDate(entity.getDueDate())
      .relatedCompany(map(entity.getRelatedCompanyId()))
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
        requestItemRepository.findAllBy(entity.getId()).collect(Collectors.toList())
      )
      .itemLots(
        requestItemLotRepository.findAllBy(entity.getId())
          .collect(Collectors.toList())
      )
      .build();
  }

  public TransactionRequest jpa(TransactionRequestEntity entity) {
    return TransactionRequest.builder()
      .id(entity.getId())
      .dueDate(entity.getDueDate())
      .relatedCompany(map(entity.getRelatedCompanyId()))
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
    @Mapping(target = "relatedCompanyId", source = "relatedCompany.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract TransactionRequestEntity jpa(TransactionRequest domain);

  @Mappings({
    @Mapping(target = "relatedCompany", source = "relatedCompanyId"),
    @Mapping(target = "station", source = "stationId")
  })
  public abstract TransactionRequestMessages.CreateRequest map(
    TransactionRequestRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "relatedCompanyId", source = "relatedCompany.id"),
    @Mapping(target = "stationId", source = "station.id")
  })
  public abstract TransactionRequestData map(
    TransactionRequest request);

  @Mappings({
    @Mapping(target = "relatedCompany", source = "relatedCompanyId"),
    @Mapping(target = "station", source = "stationId")
  })
  public abstract TransactionRequestMessages.UpdateRequest map(
    TransactionRequestRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "committedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract TransactionRequestMessages.CommitRequest map(
    TransactionRequestRequests.CommitRequest request);

  @Mappings({
    @Mapping(target = "canceledBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract TransactionRequestMessages.CancelRequest map(
    TransactionRequestRequests.CancelRequest request);

  @Mappings({
    @Mapping(target = "acceptedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract TransactionRequestMessages.AcceptRequest map(
    TransactionRequestRequests.AcceptRequest request);

  @Mappings({
    @Mapping(target = "completedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract TransactionRequestMessages.CompleteRequest map(
    TransactionRequestRequests.CompleteRequest request);

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  protected Station map(StationId warehouseStationId) {
    return stationMapper.map(warehouseStationId);
  }

  public TransactionRequest map(TransactionRequestId requestId) {
    return Optional.ofNullable(requestId)
      .map(id -> requestRepository.findBy(id)
        .orElseThrow(TransactionRequestExceptions.NotFoundException::new))
      .orElse(null);
  }

  public abstract void pass(
    TransactionRequestEntity from, @MappingTarget TransactionRequestEntity to);


}
