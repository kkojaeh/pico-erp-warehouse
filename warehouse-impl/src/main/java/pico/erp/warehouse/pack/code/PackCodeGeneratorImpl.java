package pico.erp.warehouse.pack.code;

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
    val yearDay = ((now.getYear() % 10) * 1000) + now.getDayOfYear();
    val code = String.format("%3s-%3s",
      Integer.toString(yearDay, 36),
      Long.toString(packRepository.countByCreatedToday(), 36)
    );
    return PackCode.from(code.toUpperCase().replace(' ', '0'));
  }

}
