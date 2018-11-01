package pico.erp.warehouse.pack;

import java.util.stream.Stream;

public class PackSelectorImpl implements PackSelector {

  PackRepository packRepository;

  @Override
  public Stream<Pack> select(ItemSelectOptions options) {
    return null;
  }

  @Override
  public Stream<Pack> select(ItemLotSelectOptions options) {
    return null;
  }
}
