package pico.erp.warehouse.location.rack;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface WarehouseRackExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-rack.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "warehouse-rack.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

}
