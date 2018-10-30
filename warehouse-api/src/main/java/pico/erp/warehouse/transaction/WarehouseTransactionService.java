package pico.erp.warehouse.transaction;

import javax.validation.Valid;

public interface WarehouseTransactionService {

  WarehouseTransactionData create(@Valid WarehouseTransactionRequests.CreateRequest request);

}
