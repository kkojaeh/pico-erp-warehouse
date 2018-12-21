package pico.erp.warehouse.location.level;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface LevelExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-level.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-level.code.already.exists.exception")
  class CodeAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "warehouse-level.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

}
