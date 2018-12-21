package pico.erp.warehouse.transaction.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface TransactionOrderExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-order.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-order.code.already.exists.exception")
  class CodeAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-order.cannot.modify.exception")
  class CannotModifyException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-order.cannot.commit.exception")
  class CannotCommitException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-order.cannot.cancel.exception")
  class CannotCancelException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-order.cannot.accept.exception")
  class CannotAcceptException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "warehouse-transaction-order.cannot.complete.exception")
  class CannotCompleteException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }


  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "warehouse-transaction-order.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
