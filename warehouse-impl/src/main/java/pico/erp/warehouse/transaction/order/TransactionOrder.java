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
import pico.erp.warehouse.location.station.Station;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;
import pico.erp.warehouse.transaction.request.TransactionRequest;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-order")
public class TransactionOrder implements Serializable {

  private static final long serialVersionUID = 1L;

  protected boolean committable;

  @Id
  TransactionOrderId id;

  OffsetDateTime dueDate;

  CompanyData relatedCompany;

  Station station;

  TransactionOrderStatusKind status;

  TransactionTypeKind type;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  TransactionRequest request;

  TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  public TransactionOrderMessages.CreateResponse apply(
    TransactionOrderMessages.CreateRequest request) {
    id = request.getId();
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    status = TransactionOrderStatusKind.CREATED;
    type = request.getType();
    committable = false;
    quantityCorrectionPolicy = request.getQuantityCorrectionPolicy();
    return new TransactionOrderMessages.CreateResponse(
      Arrays.asList(new TransactionOrderEvents.CreatedEvent(this.id))
    );
  }

  public TransactionOrderMessages.UpdateResponse apply(
    TransactionOrderMessages.UpdateRequest request) {
    if (!isModifiable()) {
      throw new TransactionOrderExceptions.CannotModifyException();
    }
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    quantityCorrectionPolicy = request.getQuantityCorrectionPolicy();
    return new TransactionOrderMessages.UpdateResponse(
      Arrays.asList(new TransactionOrderEvents.UpdatedEvent(this.id))
    );
  }

  public TransactionOrderMessages.CancelResponse apply(
    TransactionOrderMessages.CancelRequest request) {
    if (!isCancelable()) {
      throw new TransactionOrderExceptions.CannotCancelException();
    }
    status = TransactionOrderStatusKind.CANCELED;
    canceledBy = request.getCanceledBy();
    canceledDate = OffsetDateTime.now();
    return new TransactionOrderMessages.CancelResponse(
      Arrays.asList(new TransactionOrderEvents.CanceledEvent(this.id))
    );
  }

  public TransactionOrderMessages.AcceptResponse apply(
    TransactionOrderMessages.AcceptRequest request) {
    if (!isAcceptable()) {
      throw new TransactionOrderExceptions.CannotAcceptException();
    }
    status = TransactionOrderStatusKind.ACCEPTED;
    acceptedBy = request.getAcceptedBy();
    acceptedDate = OffsetDateTime.now();
    return new TransactionOrderMessages.AcceptResponse(
      Arrays.asList(new TransactionOrderEvents.AcceptedEvent(this.id))
    );
  }

  public TransactionOrderMessages.CompleteResponse apply(
    TransactionOrderMessages.CompleteRequest request) {
    if (!isCompletable()) {
      throw new TransactionOrderExceptions.CannotCompleteException();
    }
    status = TransactionOrderStatusKind.COMPLETED;
    completedBy = request.getCompletedBy();
    completedDate = OffsetDateTime.now();
    return new TransactionOrderMessages.CompleteResponse(
      Arrays.asList(new TransactionOrderEvents.CompletedEvent(this.id))
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
