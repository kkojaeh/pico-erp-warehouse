package pico.erp.warehouse.pack;

import java.math.BigDecimal;
import java.util.stream.Stream;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;

public interface PackSelector {

  Stream<Pack> select(ItemSelectOptions options);

  Stream<Pack> select(ItemLotSelectOptions options);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  class ItemSelectOptions {

    @NotNull
    ItemId itemId;

    @Min(0)
    @NotNull
    BigDecimal quantity;

    @NotNull
    TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  class ItemLotSelectOptions {

    @NotNull
    ItemLotId itemLotId;

    @Min(0)
    @NotNull
    BigDecimal quantity;

    @NotNull
    TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

}
