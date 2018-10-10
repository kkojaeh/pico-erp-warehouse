package pico.erp.warehouse;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import pico.erp.audit.AuditConfiguration;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.Public;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.data.Role;
import pico.erp.shared.impl.ApplicationImpl;

@Slf4j
@SpringBootConfigs
public class WarehouseApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "warehouse/application";

  public static final String CONFIG_NAME_PROPERTY = "spring.config.name=warehouse/application";

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
      .entity(WarehouseRoles.class)
      .build();
  }

  @Bean
  @Public
  public Role facilityAccessorRole() {
    return WarehouseRoles.WAREHOUSE_ACCESSOR;
  }

  @Bean
  @Public
  public Role facilityManagerRole() {
    return WarehouseRoles.WAREHOUSE_MANAGER;
  }

  @Override
  public int getOrder() {
    return 3;
  }

  @Override
  public boolean isWeb() {
    return false;
  }

  @Override
  public pico.erp.shared.Application start(String... args) {
    return new ApplicationImpl(application().run(args));
  }

}
