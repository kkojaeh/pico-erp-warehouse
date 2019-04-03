package pico.erp.warehouse.transaction.request;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.jpa.QueryDslJpaSupport;
import pico.erp.warehouse.transaction.request.item.QTransactionRequestItemEntity;

@Service
@ComponentBean
@Transactional(readOnly = true)
@Validated
public class TransactionRequestQueryJpa implements TransactionRequestQuery {

  private final QTransactionRequestEntity transactionRequest = QTransactionRequestEntity.transactionRequestEntity;

  private final QTransactionRequestItemEntity transactionRequestItem = QTransactionRequestItemEntity.transactionRequestItemEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public Page<TransactionRequestView> retrieve(TransactionRequestView.Filter filter,
    Pageable pageable) {
    val query = new JPAQuery<TransactionRequestView>(entityManager);
    val select = Projections.bean(TransactionRequestView.class,
      transactionRequest.id,
      transactionRequest.code,
      transactionRequest.dueDate,
      transactionRequest.transactionCompanyId,
      transactionRequest.stationId,
      transactionRequest.status,
      transactionRequest.type,
      transactionRequest.quantityCorrectionPolicy,
      transactionRequest.committedBy,
      transactionRequest.committedDate,
      transactionRequest.canceledBy,
      transactionRequest.canceledDate,
      transactionRequest.acceptedBy,
      transactionRequest.acceptedDate,
      transactionRequest.completedBy,
      transactionRequest.completedDate
    );
    query.select(select);
    query.from(transactionRequest);

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getCode())) {
      builder.and(
        transactionRequest.code.value.likeIgnoreCase(
          queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%")
        )
      );
    }

    if (filter.getItemId() != null) {
      builder.and(
        transactionRequest.id.in(
          JPAExpressions.select(transactionRequestItem.requestId)
            .from(transactionRequestItem)
            .where(transactionRequestItem.itemId.eq(filter.getItemId()))
        )
      );
    }
    if (filter.getTransactionCompanyId() != null) {
      builder.and(
        transactionRequest.transactionCompanyId.eq(filter.getTransactionCompanyId())
      );
    }
    if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
      builder.and(transactionRequest.status.in(filter.getStatuses()));
    }
    if (filter.getType() != null) {
      builder.and(transactionRequest.type.eq(filter.getType()));
    }
    if (filter.getCreateBy() != null) {
      builder.and(transactionRequest.createdBy.id.eq(filter.getCreateBy().getValue()));
    }
    if (filter.getStartCreatedDate() != null) {
      builder.and(transactionRequest.createdDate.goe(filter.getStartCreatedDate()));
    }
    if (filter.getEndCreatedDate() != null) {
      builder.and(transactionRequest.createdDate.loe(filter.getEndCreatedDate()));
    }
    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
