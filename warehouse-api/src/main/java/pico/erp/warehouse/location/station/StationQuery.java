package pico.erp.warehouse.location.station;

import java.util.List;
import javax.validation.constraints.NotNull;
import pico.erp.shared.data.LabeledValuable;

public interface StationQuery {

  List<? extends LabeledValuable> asLabels(@NotNull String keyword, long limit);

}
