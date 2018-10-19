package pico.erp.warehouse.pack.code;

import pico.erp.warehouse.pack.WarehousePack;
import pico.erp.warehouse.pack.WarehousePackCode;

public interface WarehousePackCodeGenerator {

  WarehousePackCode generate(WarehousePack warehousePack);
}
