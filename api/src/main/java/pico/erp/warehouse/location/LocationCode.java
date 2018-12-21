package pico.erp.warehouse.location;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pico.erp.shared.TypeDefinitions;
import pico.erp.warehouse.location.bay.BayCode;
import pico.erp.warehouse.location.level.LevelCode;
import pico.erp.warehouse.location.rack.RackCode;
import pico.erp.warehouse.location.site.SiteCode;
import pico.erp.warehouse.location.station.StationCode;
import pico.erp.warehouse.location.zone.ZoneCode;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "value")
@ToString
public class LocationCode implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @Size(min = 1, max = TypeDefinitions.ID_LENGTH)
  @NotNull
  private String value;

  public static LocationCode from(@NonNull String value) {
    return new LocationCode(value);
  }

  public static LocationCode from(@NonNull SiteCode code) {
    return new LocationCode(code.getValue());
  }

  public LocationCode with(@NonNull ZoneCode code) {
    return new LocationCode(
      String.format("%s-%s", value, code.getValue())
    );
  }

  public LocationCode with(@NonNull RackCode code) {
    return new LocationCode(
      String.format("%s-%02d", value, code.getValue())
    );
  }

  public LocationCode with(@NonNull BayCode code) {
    return new LocationCode(
      String.format("%s-%02d", value, code.getValue())
    );
  }

  public LocationCode with(@NonNull LevelCode code) {
    return new LocationCode(
      String.format("%s-%02d", value, code.getValue())
    );
  }

  public LocationCode with(@NonNull StationCode code) {
    return new LocationCode(
      String.format("%s-%s", value, code.getValue())
    );
  }
}
