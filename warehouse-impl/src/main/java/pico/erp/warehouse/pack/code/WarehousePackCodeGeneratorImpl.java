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
    val date = ((now.getYear() % 10) * 1000) + now.getDayOfYear();
    val radixDate = Integer.toString(date, 36);
    val prefix = ("000".substring(radixDate.length()) + radixDate).toUpperCase();
    val count = packRepository.countByCreatedToday();
    val code = String.format("%s-%04d", prefix, count);
    return WarehousePackCode.from(code);
  }

}
