package pico.erp.warehouse.transaction.order;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionOrderQuery {

  Page<TransactionOrderView> retrieve(@NotNull TransactionOrderView.Filter filter,
    @NotNull Pageable pageable);

}
