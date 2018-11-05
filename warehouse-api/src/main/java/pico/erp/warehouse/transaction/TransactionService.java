package pico.erp.warehouse.transaction;

import javax.validation.Valid;

public interface TransactionService {

  TransactionData create(@Valid TransactionRequests.CreateRequest request);

}
