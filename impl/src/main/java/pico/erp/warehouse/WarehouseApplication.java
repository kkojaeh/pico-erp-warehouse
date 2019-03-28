package pico.erp.warehouse;

import kkojaeh.spring.boot.component.Give;
import kkojaeh.spring.boot.component.SpringBootComponent;
import kkojaeh.spring.boot.component.SpringBootComponentBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pico.erp.shared.SharedConfiguration;
import pico.erp.shared.data.Role;
import pico.erp.warehouse.WarehouseApi.Roles;

@Slf4j
@SpringBootComponent("warehouse")
@EntityScan
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
@SpringBootApplication
@Import(value = {
  SharedConfiguration.class
})
public class WarehouseApplication {


  public static void main(String[] args) {
    new SpringBootComponentBuilder()
      .component(WarehouseApplication.class)
      .run(args);
  }


  @Bean
  @Give
  public Role warehouseAccessorRole() {
    return Roles.WAREHOUSE_ACCESSOR;
  }

  @Bean
  @Give
  public Role warehouseManagerRole() {
    return Roles.WAREHOUSE_MANAGER;
  }

  @Bean
  @Give
  public Role warehouseTransactionRequesterRole() {
    return Roles.WAREHOUSE_TRANSACTION_REQUESTER;
  }

  @Bean
  @Give
  public Role warehouseWorkerRole() {
    return Roles.WAREHOUSE_WORKER;
  }

}
