package pico.erp.warehouse.pack.code;

import pico.erp.warehouse.pack.Pack;
import pico.erp.warehouse.pack.PackCode;

public interface PackCodeGenerator {

  PackCode generate(Pack warehousePack);
}
