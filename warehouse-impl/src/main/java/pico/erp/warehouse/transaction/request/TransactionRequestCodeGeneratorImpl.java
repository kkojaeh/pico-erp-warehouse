package pico.erp.warehouse.transaction.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
    val begin = OffsetDateTime.now().with(LocalTime.MIN);
    val end = OffsetDateTime.now().with(LocalTime.MAX);
    val count = transactionRequestRepository.countCreatedBetween(begin, end);
    val date = DateTimeFormatter.ofPattern("yyMMdd").format(LocalDate.now());
    val code = String.format("%s-%04d", date, count + 1).toUpperCase();
    return TransactionRequestCode.from(code);
  }

}
