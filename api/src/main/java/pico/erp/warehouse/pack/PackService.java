package pico.erp.warehouse.pack;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface PackService {

  PackData create(@Valid PackRequests.CreateRequest request);

  void delete(@Valid PackRequests.DeleteRequest request);

  boolean exists(@NotNull PackId id);

  PackData get(@NotNull PackId id);

  void pack(@Valid PackRequests.PackRequest request);

  void put(@Valid PackRequests.PutRequest request);

}
