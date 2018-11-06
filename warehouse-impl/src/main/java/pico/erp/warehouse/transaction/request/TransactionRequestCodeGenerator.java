package pico.erp.warehouse.transaction.request;

public interface TransactionRequestCodeGenerator {

  TransactionRequestCode generate(TransactionRequest transactionRequest);

}
