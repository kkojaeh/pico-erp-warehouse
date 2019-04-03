package pico.erp.warehouse;

import kkojaeh.spring.boot.component.ComponentBean;
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
import pico.erp.ComponentDefinition;
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
public class WarehouseApplication implements ComponentDefinition {


  public static void main(String[] args) {
    new SpringBootComponentBuilder()
      .component(WarehouseApplication.class)
      .run(args);
  }

  @Override
  public Class<?> getComponentClass() {
    return WarehouseApplication.class;
  }


  @Bean
  @ComponentBean(host = false)
  public Role warehouseAccessorRole() {
    return Roles.WAREHOUSE_ACCESSOR;
  }

  @Bean
  @ComponentBean(host = false)
  public Role warehouseManagerRole() {
    return Roles.WAREHOUSE_MANAGER;
  }

  @Bean
  @ComponentBean(host = false)
  public Role warehouseTransactionRequesterRole() {
    return Roles.WAREHOUSE_TRANSACTION_REQUESTER;
  }

  @Bean
  @ComponentBean(host = false)
  public Role warehouseWorkerRole() {
    return Roles.WAREHOUSE_WORKER;
  }

}
