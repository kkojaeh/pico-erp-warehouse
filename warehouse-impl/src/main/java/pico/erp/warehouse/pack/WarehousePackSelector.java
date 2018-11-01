package pico.erp.warehouse.pack;

import java.math.BigDecimal;
import java.util.stream.Stream;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pico.erp.item.ItemId;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.WarehouseTransactionQuantityCorrectionPolicyKind;

public interface WarehousePackSelector {

  Stream<WarehousePack> select(ItemSelectOptions options);

  Stream<WarehousePack> select(ItemLotSelectOptions options);

  @Data
  @Builder
  class ItemSelectOptions {

    @NotNull
    ItemId itemId;

    @Min(0)
    @NotNull
    BigDecimal quantity;

    @NotNull
    WarehouseTransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  @Data
  @Builder
  class ItemLotSelectOptions {

    @NotNull
    ItemLotId itemLotId;

    @Min(0)
    @NotNull
    BigDecimal quantity;

    @NotNull
    WarehouseTransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

}
