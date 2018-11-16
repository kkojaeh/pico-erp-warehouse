package pico.erp.warehouse.pack.code;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pico.erp.warehouse.pack.Pack;
import pico.erp.warehouse.pack.PackCode;
import pico.erp.warehouse.pack.PackRepository;

@Component
public class PackCodeGeneratorImpl implements PackCodeGenerator {

  @Lazy
  @Autowired
  protected PackRepository packRepository;

  @Override
  public PackCode generate(Pack warehousePack) {
    val now = OffsetDateTime.now();
    val begin = now.with(LocalTime.MIN);
    val end = now.with(LocalTime.MAX);

    val date =
      Integer.toString(now.getYear() - 1900, 36) + Integer.toString(now.getMonthValue(), 16)
        + Integer.toString(now.getDayOfMonth(), 36);
    val code = String.format("%s-%3s",
      date,
      Long.toString(packRepository.countCreatedBetween(begin, end), 36)
    );
    return PackCode.from(code.toUpperCase().replace(' ', '0'));
  }

}
