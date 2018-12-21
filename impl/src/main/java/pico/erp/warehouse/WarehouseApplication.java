package pico.erp.warehouse;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import pico.erp.audit.AuditConfiguration;
import pico.erp.company.CompanyApi;
import pico.erp.item.ItemApi;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.Public;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.data.Role;
import pico.erp.shared.impl.ApplicationImpl;
import pico.erp.user.UserApi;
import pico.erp.warehouse.WarehouseApi.Roles;

@Slf4j
@SpringBootConfigs
public class WarehouseApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "warehouse/application";

  public static final Properties DEFAULT_PROPERTIES = new Properties();

  static {
    DEFAULT_PROPERTIES.put("spring.config.name", CONFIG_NAME);
  }

  public static SpringApplication application() {
    return new SpringApplicationBuilder(WarehouseApplication.class)
      .properties(DEFAULT_PROPERTIES)
      .web(false)
      .build();
  }

  public static void main(String[] args) {
    application().run(args);
  }

  @Bean
  @Public
  public AuditConfiguration auditConfiguration() {
    return AuditConfiguration.builder()
      .packageToScan("pico.erp.warehouse")
      .entity(Roles.class)
      .build();
  }

  @Override
  public Set<ApplicationId> getDependencies() {
    return Stream.of(
      UserApi.ID,
      CompanyApi.ID,
      ItemApi.ID
    ).collect(Collectors.toSet());
  }

  @Override
  public ApplicationId getId() {
    return WarehouseApi.ID;
  }

  @Override
  public boolean isWeb() {
    return false;
  }

  @Bean
  @Public
  public Role warehouseAccessorRole() {
    return Roles.WAREHOUSE_ACCESSOR;
  }

  @Bean
  @Public
  public Role warehouseManagerRole() {
    return Roles.WAREHOUSE_MANAGER;
  }

  @Bean
  @Public
  public Role warehouseTransactionRequesterRole() {
    return Roles.WAREHOUSE_TRANSACTION_REQUESTER;
  }

  @Bean
  @Public
  public Role warehouseWorkerRole() {
    return Roles.WAREHOUSE_WORKER;
  }

  @Override
  public pico.erp.shared.Application start(String... args) {
    return new ApplicationImpl(application().run(args));
  }

}
