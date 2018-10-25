package pico.erp.warehouse;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.ApplicationInitializer;
import pico.erp.warehouse.location.bay.WarehouseBayRequests;
import pico.erp.warehouse.location.bay.WarehouseBayService;
import pico.erp.warehouse.location.level.WarehouseLevelRequests;
import pico.erp.warehouse.location.level.WarehouseLevelService;
import pico.erp.warehouse.location.rack.WarehouseRackRequests;
import pico.erp.warehouse.location.rack.WarehouseRackService;
import pico.erp.warehouse.location.site.WarehouseSiteRequests;
import pico.erp.warehouse.location.site.WarehouseSiteService;
import pico.erp.warehouse.location.station.WarehouseStationRequests;
import pico.erp.warehouse.location.station.WarehouseStationService;
import pico.erp.warehouse.location.zone.WarehouseZoneRequests;
import pico.erp.warehouse.location.zone.WarehouseZoneService;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Transactional
@Configuration
@Profile({"!development", "!production"})
public class TestDataInitializer implements ApplicationInitializer {

  @Lazy
  @Autowired
  private WarehouseSiteService warehouseSiteService;

  @Lazy
  @Autowired
  private WarehouseZoneService warehouseZoneService;

  @Lazy
  @Autowired
  private WarehouseRackService warehouseRackService;

  @Lazy
  @Autowired
  private WarehouseBayService warehouseBayService;

  @Lazy
  @Autowired
  private WarehouseLevelService warehouseLevelService;

  @Lazy
  @Autowired
  private WarehouseStationService warehouseStationService;


  @Autowired
  private DataProperties dataProperties;

  @Override
  public void initialize() {
    dataProperties.sites.forEach(warehouseSiteService::create);
    dataProperties.zones.forEach(warehouseZoneService::create);
    dataProperties.racks.forEach(warehouseRackService::create);
    dataProperties.bays.forEach(warehouseBayService::create);
    dataProperties.levels.forEach(warehouseLevelService::create);
    dataProperties.stations.forEach(warehouseStationService::create);
  }

  @Data
  @Configuration
  @ConfigurationProperties("data")
  public static class DataProperties {

    List<WarehouseSiteRequests.CreateRequest> sites = new LinkedList<>();

    List<WarehouseZoneRequests.CreateRequest> zones = new LinkedList<>();

    List<WarehouseRackRequests.CreateRequest> racks = new LinkedList<>();

    List<WarehouseBayRequests.CreateRequest> bays = new LinkedList<>();

    List<WarehouseLevelRequests.CreateRequest> levels = new LinkedList<>();

    List<WarehouseStationRequests.CreateRequest> stations = new LinkedList<>();

  }

}
