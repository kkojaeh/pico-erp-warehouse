package pico.erp.warehouse.pack.code;

import java.time.OffsetDateTime;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pico.erp.warehouse.pack.WarehousePack;
import pico.erp.warehouse.pack.WarehousePackCode;
import pico.erp.warehouse.pack.WarehousePackRepository;

@Component
public class WarehousePackCodeGeneratorImpl implements WarehousePackCodeGenerator {

  @Lazy
  @Autowired
  protected WarehousePackRepository packRepository;

  @Override
  public WarehousePackCode generate(WarehousePack warehousePack) {
    val now = OffsetDateTime.now();
    val yearDay = ((now.getYear() % 10) * 1000) + now.getDayOfYear();
    val code = String.format("%3s-%3s",
      Integer.toString(yearDay, 36),
      Long.toString(packRepository.countByCreatedToday(), 36)
    );
    return WarehousePackCode.from(code.toUpperCase().replace(' ', '0'));
  }

}
