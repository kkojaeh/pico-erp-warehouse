package pico.erp.warehouse;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.data.Role;

public final class WarehouseApi {

  public final static ApplicationId ID = ApplicationId.from("warehouse");

  @RequiredArgsConstructor
  public enum Roles implements Role {

    WAREHOUSE_MANAGER,

    WAREHOUSE_ACCESSOR,

    WAREHOUSE_WORKER,

    WAREHOUSE_TRANSACTION_REQUESTER;


    @Id
    @Getter
    private final String id = name();

  }
}
