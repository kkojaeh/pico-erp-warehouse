package pico.erp.warehouse.transaction.request;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRequestQuery {

  Page<TransactionRequestView> retrieve(@NotNull TransactionRequestView.Filter filter,
    @NotNull Pageable pageable);

}
