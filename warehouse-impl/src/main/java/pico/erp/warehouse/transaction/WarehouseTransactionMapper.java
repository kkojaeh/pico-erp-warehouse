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
import pico.erp.warehouse.location.station.WarehouseStation;
import pico.erp.warehouse.location.station.WarehouseStationEntity;
import pico.erp.warehouse.location.station.WarehouseStationId;
import pico.erp.warehouse.location.station.WarehouseStationMapper;
import pico.erp.warehouse.pack.code.WarehousePackCodeGenerator;
import pico.erp.warehouse.transaction.WarehouseTransactionMessages.CreateRequest;

@Mapper
public abstract class WarehouseTransactionMapper {

  @Autowired
  protected WarehousePackCodeGenerator codeGenerator;

  @Lazy
  @Autowired
  protected ItemLotService itemLotService;

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Autowired
  protected WarehouseStationMapper stationMapper;

  public WarehouseTransaction jpa(WarehouseTransactionEntity entity) {
    return WarehouseTransaction.builder()
      .id(entity.getId())
      .itemLot(map(entity.getItemLotId()))
      .quantity(entity.getQuantity())
      .transactedBy(entity.getTransactedBy())
      .transactedDate(entity.getTransactedDate())
      .quantity(entity.getQuantity())
      .type(entity.getType())
      .relatedCompany(map(entity.getRelatedCompanyId()))
      .station(jpa(entity.getStation()))
      .build();
  }

  @Mappings({
    @Mapping(target = "relatedCompanyId", source = "relatedCompany.id"),
    @Mapping(target = "itemLotId", source = "itemLot.id"),
    @Mapping(target = "itemId", source = "itemLot.itemId")
  })
  public abstract WarehouseTransactionEntity jpa(WarehouseTransaction domain);

  protected WarehouseStation jpa(WarehouseStationEntity entity) {
    return Optional.ofNullable(entity)
      .map(stationMapper::jpa)
      .orElse(null);
  }

  protected WarehouseStationEntity jpa(WarehouseStation domain) {
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
    @Mapping(target = "relatedCompanyId", source = "relatedCompany.id"),
    @Mapping(target = "stationId", source = "station.id"),
  })
  public abstract WarehouseTransactionData map(WarehouseTransaction transaction);

  @Mappings({
    @Mapping(target = "relatedCompany", source = "relatedCompanyId"),
    @Mapping(target = "station", source = "stationId"),
    @Mapping(target = "itemLot", source = "itemLotId"),
    @Mapping(target = "transactedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract CreateRequest map(
    WarehouseTransactionRequests.CreateRequest request);

  protected WarehouseStation map(WarehouseStationId warehouseStationId) {
    return stationMapper.map(warehouseStationId);
  }

  public abstract void pass(
    WarehouseTransactionEntity from, @MappingTarget WarehouseTransactionEntity to);


}
