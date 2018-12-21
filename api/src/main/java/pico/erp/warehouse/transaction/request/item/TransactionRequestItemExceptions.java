package pico.erp.warehouse.transaction.request.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface TransactionRequestItemExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-request-item.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-request-item.cannot.create.exception")
  class CannotCreateException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-request-item.cannot.update.exception")
  class CannotUpdateException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-request-item.cannot.delete.exception")
  class CannotDeleteException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "warehouse-transaction-request-item.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
