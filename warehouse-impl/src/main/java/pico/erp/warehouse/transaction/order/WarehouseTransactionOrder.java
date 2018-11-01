package pico.erp.warehouse.transaction.order;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.company.CompanyData;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStation;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequest;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-order")
public class WarehouseTransactionOrder implements Serializable {

  private static final long serialVersionUID = 1L;

  protected boolean committable;

  @Id
  WarehouseTransactionOrderId id;

  OffsetDateTime dueDate;

  CompanyData relatedCompany;

  WarehouseStation station;

  WarehouseTransactionOrderStatusKind status;

  WarehouseTransactionTypeKind type;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  WarehouseTransactionRequest request;

  public WarehouseTransactionOrderMessages.CreateResponse apply(
    WarehouseTransactionOrderMessages.CreateRequest request) {
    id = request.getId();
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    status = WarehouseTransactionOrderStatusKind.CREATED;
    type = request.getType();
    committable = false;
    return new WarehouseTransactionOrderMessages.CreateResponse(
      Arrays.asList(new WarehouseTransactionOrderEvents.CreatedEvent(this.id))
    );
  }

  public WarehouseTransactionOrderMessages.UpdateResponse apply(
    WarehouseTransactionOrderMessages.UpdateRequest request) {
    if (!isModifiable()) {
      throw new WarehouseTransactionOrderExceptions.CannotModifyException();
    }
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    return new WarehouseTransactionOrderMessages.UpdateResponse(
      Arrays.asList(new WarehouseTransactionOrderEvents.UpdatedEvent(this.id))
    );
  }

  public WarehouseTransactionOrderMessages.CommitResponse apply(
    WarehouseTransactionOrderMessages.CommitRequest request) {
    if (!isCancelable()) {
      throw new WarehouseTransactionOrderExceptions.CannotCommitException();
    }
    status = WarehouseTransactionOrderStatusKind.COMMITTED;
    canceledBy = request.getCommittedBy();
    canceledDate = OffsetDateTime.now();
    return new WarehouseTransactionOrderMessages.CommitResponse(
      Arrays.asList(new WarehouseTransactionOrderEvents.CommittedEvent(this.id))
    );
  }

  public WarehouseTransactionOrderMessages.CancelResponse apply(
    WarehouseTransactionOrderMessages.CancelRequest request) {
    if (!isCancelable()) {
      throw new WarehouseTransactionOrderExceptions.CannotCancelException();
    }
    status = WarehouseTransactionOrderStatusKind.CANCELED;
    canceledBy = request.getCanceledBy();
    canceledDate = OffsetDateTime.now();
    return new WarehouseTransactionOrderMessages.CancelResponse(
      Arrays.asList(new WarehouseTransactionOrderEvents.CanceledEvent(this.id))
    );
  }

  public WarehouseTransactionOrderMessages.AcceptResponse apply(
    WarehouseTransactionOrderMessages.AcceptRequest request) {
    if (!isAcceptable()) {
      throw new WarehouseTransactionOrderExceptions.CannotAcceptException();
    }
    status = WarehouseTransactionOrderStatusKind.ACCEPTED;
    acceptedBy = request.getAcceptedBy();
    acceptedDate = OffsetDateTime.now();
    return new WarehouseTransactionOrderMessages.AcceptResponse(
      Arrays.asList(new WarehouseTransactionOrderEvents.AcceptedEvent(this.id))
    );
  }

  public WarehouseTransactionOrderMessages.CompleteResponse apply(
    WarehouseTransactionOrderMessages.CompleteRequest request) {
    if (!isCompletable()) {
      throw new WarehouseTransactionOrderExceptions.CannotCompleteException();
    }
    status = WarehouseTransactionOrderStatusKind.COMPLETED;
    completedBy = request.getCompletedBy();
    completedDate = OffsetDateTime.now();
    return new WarehouseTransactionOrderMessages.CompleteResponse(
      Arrays.asList(new WarehouseTransactionOrderEvents.CompletedEvent(this.id))
    );
  }

  public boolean isAcceptable() {
    return status.isAcceptable();
  }

  public boolean isCancelable() {
    return status.isCancelable();
  }

  public boolean isCompletable() {
    return status.isCompletable();
  }

  public boolean isModifiable() {
    return status.isModifiable();
  }


}
