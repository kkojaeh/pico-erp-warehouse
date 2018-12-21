package pico.erp.warehouse.transaction.order;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TransactionOrderCodeGeneratorImpl implements TransactionOrderCodeGenerator {

  @Lazy
  @Autowired
  private TransactionOrderRepository transactionOrderRepository;

  @Override
  public TransactionOrderCode generate(TransactionOrder transactionOrder) {
    val begin = OffsetDateTime.now().with(LocalTime.MIN);
    val end = OffsetDateTime.now().with(LocalTime.MAX);
    val count = transactionOrderRepository.countCreatedBetween(begin, end);
    val now = LocalDate.now();
    val date =
      Integer.toString(now.getYear() - 1900, 36) + Integer.toString(now.getMonthValue(), 16)
        + Integer.toString(now.getDayOfMonth(), 36);
    val code = String.format("%s-%04d", date, count + 1).toUpperCase();
    return TransactionOrderCode.from(code);
  }

}
