package pico.erp.warehouse.transaction;

import javax.validation.Valid;

public interface WarehouseTransactionService {

  WarehouseTransactionData inbound(@Valid WarehouseTransactionRequests.InboundRequest request);

  WarehouseTransactionData outbound(@Valid WarehouseTransactionRequests.OutboundRequest request);

}
