package pico.erp.warehouse.location.bay;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "value")
@ToString
public class BayCode implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @Min(1)
  @Max(99)
  @NotNull
  private Integer value;

  public static BayCode from(@NonNull Integer value) {
    return new BayCode(value);
  }

}
