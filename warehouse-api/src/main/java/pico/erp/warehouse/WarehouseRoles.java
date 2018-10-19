package pico.erp.warehouse;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

@RequiredArgsConstructor
public enum WarehouseRoles implements Role {

  WAREHOUSE_MANAGER,

  WAREHOUSE_ACCESSOR,

  WAREHOUSE_WORKER;


  @Id
  @Getter
  private final String id = name();

}
