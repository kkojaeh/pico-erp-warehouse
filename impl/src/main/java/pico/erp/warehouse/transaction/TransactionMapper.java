package pico.erp.warehouse.transaction;

import java.util.Optional;
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
import pico.erp.item.lot.ItemLotData;
import pico.erp.item.lot.ItemLotId;
import pico.erp.item.lot.ItemLotService;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.Station;
import pico.erp.warehouse.location.station.StationEntity;
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.location.station.StationMapper;
import pico.erp.warehouse.pack.PackCodeGenerator;
import pico.erp.warehouse.transaction.TransactionMessages.CreateRequest;

@Mapper
public abstract class TransactionMapper {

  @Autowired
  protected PackCodeGenerator codeGenerator;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Autowired
  protected StationMapper stationMapper;

  public Transaction jpa(TransactionEntity entity) {
    return Transaction.builder()
      .id(entity.getId())
      .itemLot(map(entity.getItemLotId()))
      .quantity(entity.getQuantity())
      .transactedBy(entity.getTransactedBy())
      .transactedDate(entity.getTransactedDate())
      .quantity(entity.getQuantity())
      .type(entity.getType())
      .transactionCompany(map(entity.getTransactionCompanyId()))
      .station(jpa(entity.getStation()))
      .build();
  }

  @Mappings({
    @Mapping(target = "transactionCompanyId", source = "transactionCompany.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id"),
    @Mapping(target = "itemId", source = "itemLot.itemId")
  })
  public abstract TransactionEntity jpa(Transaction domain);

  protected Station jpa(StationEntity entity) {
    return Optional.ofNullable(entity)
      .map(stationMapper::jpa)
      .orElse(null);
  }

  protected StationEntity jpa(Station domain) {
    return stationMapper.jpa(domain);
  }

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "itemLotId", source = "itemLot.id"),
    @Mapping(target = "transactionCompanyId", source = "transactionCompany.id"),
    @Mapping(target = "stationId", source = "station.id"),
  })
  public abstract TransactionData map(Transaction transaction);

  @Mappings({
    @Mapping(target = "transactionCompany", source = "transactionCompanyId"),
    @Mapping(target = "station", source = "stationId"),
    @Mapping(target = "itemLot", source = "itemLotId"),
    @Mapping(target = "transactedBy", expression = "java(auditorAware.getCurrentAuditor().get())")
  })
  public abstract CreateRequest map(
    TransactionRequests.CreateRequest request);

  protected Station map(StationId warehouseStationId) {
    return stationMapper.map(warehouseStationId);
  }

  public abstract void pass(
    TransactionEntity from, @MappingTarget TransactionEntity to);


}
