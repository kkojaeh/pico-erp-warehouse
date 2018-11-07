package pico.erp.warehouse.transaction.order;

public interface TransactionOrderCodeGenerator {

  TransactionOrderCode generate(TransactionOrder transactionOrder);

}
