package pico.erp.warehouse.transaction.request;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.company.CompanyData;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStation;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;

@Builder
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-request")
public class WarehouseTransactionRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionRequestId id;

  OffsetDateTime dueDate;

  CompanyData relatedCompany;

  WarehouseStation station;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  WarehouseTransactionRequestStatusKind status;

  WarehouseTransactionTypeKind type;

  public WarehouseTransactionRequest() {
  }

  public WarehouseTransactionRequestMessages.InboundResponse apply(
    WarehouseTransactionRequestMessages.InboundRequest request) {
    id = request.getId();
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    status = WarehouseTransactionRequestStatusKind.CREATED;
    type = WarehouseTransactionTypeKind.INBOUND;
    return new WarehouseTransactionRequestMessages.InboundResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.CreatedEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.OutboundResponse apply(
    WarehouseTransactionRequestMessages.OutboundRequest request) {
    id = request.getId();
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    status = WarehouseTransactionRequestStatusKind.CREATED;
    type = WarehouseTransactionTypeKind.OUTBOUND;
    return new WarehouseTransactionRequestMessages.OutboundResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.CreatedEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.UpdateResponse apply(
    WarehouseTransactionRequestMessages.UpdateRequest request) {
    if (!status.isModifiable()) {
      throw new WarehouseTransactionRequestExceptions.CannotModifyException();
    }
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    return new WarehouseTransactionRequestMessages.UpdateResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.UpdatedEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.CommitResponse apply(
    WarehouseTransactionRequestMessages.CommitRequest request) {
    if (!status.isCommittable()) {
      throw new WarehouseTransactionRequestExceptions.CannotCommitException();
    }
    committedBy = request.getCommittedBy();
    committedDate = OffsetDateTime.now();
    return new WarehouseTransactionRequestMessages.CommitResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.CommittedEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.CancelResponse apply(
    WarehouseTransactionRequestMessages.CancelRequest request) {
    if (!status.isCommittable()) {
      throw new WarehouseTransactionRequestExceptions.CannotCommitException();
    }
    canceledBy = request.getCanceledBy();
    canceledDate = OffsetDateTime.now();
    return new WarehouseTransactionRequestMessages.CancelResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.CanceledEvent(this.id))
    );
  }

}
