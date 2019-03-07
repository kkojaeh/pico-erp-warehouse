package pico.erp.warehouse;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import pico.erp.shared.ApplicationInitializer;
import pico.erp.warehouse.location.bay.BayRequests;
import pico.erp.warehouse.location.bay.BayService;
import pico.erp.warehouse.location.level.LevelRequests;
import pico.erp.warehouse.location.level.LevelService;
import pico.erp.warehouse.location.rack.RackRequests;
import pico.erp.warehouse.location.rack.RackService;
import pico.erp.warehouse.location.site.SiteRequests;
import pico.erp.warehouse.location.site.SiteService;
import pico.erp.warehouse.location.station.StationRequests;
import pico.erp.warehouse.location.station.StationService;
import pico.erp.warehouse.location.zone.ZoneRequests;
import pico.erp.warehouse.location.zone.ZoneService;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@Profile({"test-data"})
public class TestDataInitializer implements ApplicationInitializer {

  @Lazy
  @Autowired
  private SiteService warehouseSiteService;

  @Lazy
  @Autowired
  private ZoneService warehouseZoneService;

  @Lazy
  @Autowired
  private RackService warehouseRackService;

  @Lazy
  @Autowired
  private BayService warehouseBayService;

  @Lazy
  @Autowired
  private LevelService warehouseLevelService;

  @Lazy
  @Autowired
  private StationService warehouseStationService;


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

    List<SiteRequests.CreateRequest> sites = new LinkedList<>();

    List<ZoneRequests.CreateRequest> zones = new LinkedList<>();

    List<RackRequests.CreateRequest> racks = new LinkedList<>();

    List<BayRequests.CreateRequest> bays = new LinkedList<>();

    List<LevelRequests.CreateRequest> levels = new LinkedList<>();

    List<StationRequests.CreateRequest> stations = new LinkedList<>();

  }

}
