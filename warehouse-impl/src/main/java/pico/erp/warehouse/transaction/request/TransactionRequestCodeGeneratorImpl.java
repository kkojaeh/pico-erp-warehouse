package pico.erp.warehouse.transaction.request;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TransactionRequestCodeGeneratorImpl implements TransactionRequestCodeGenerator {

  @Lazy
  @Autowired
  private TransactionRequestRepository transactionRequestRepository;

  @Override
  public TransactionRequestCode generate(TransactionRequest transactionRequest) {
    val now = OffsetDateTime.now();
    val begin = now.with(LocalTime.MIN);
    val end = now.with(LocalTime.MAX);
    val count = transactionRequestRepository.countCreatedBetween(begin, end);
    val date =
      Integer.toString(now.getYear() - 1900, 36) + Integer.toString(now.getMonthValue(), 16)
        + Integer.toString(now.getDayOfMonth(), 36);
    val code = String.format("%s-%04d", date, count + 1).toUpperCase();
    return TransactionRequestCode.from(code);
  }

}
