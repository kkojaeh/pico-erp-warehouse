package pico.erp.warehouse.pack;

import java.util.stream.Stream;

public class WarehousePackSelectorImpl implements WarehousePackSelector {

  WarehousePackRepository packRepository;

  @Override
  public Stream<WarehousePack> select(ItemSelectOptions options) {
    return null;
  }

  @Override
  public Stream<WarehousePack> select(ItemLotSelectOptions options) {
    return null;
  }
}
