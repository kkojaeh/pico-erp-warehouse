package pico.erp.warehouse.transaction.order;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.jpa.QueryDslJpaSupport;
import pico.erp.warehouse.transaction.order.TransactionOrderView.Filter;
import pico.erp.warehouse.transaction.order.item.QTransactionOrderItemEntity;

@Service
@Public
@Transactional(readOnly = true)
@Validated
public class TransactionOrderQueryJpa implements TransactionOrderQuery {

  private final QTransactionOrderEntity transactionOrder = QTransactionOrderEntity.transactionOrderEntity;

  private final QTransactionOrderItemEntity transactionOrderItem = QTransactionOrderItemEntity.transactionOrderItemEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;


  @Override
  public Page<TransactionOrderView> retrieve(Filter filter, Pageable pageable) {
    val query = new JPAQuery<TransactionOrderView>(entityManager);
    val select = Projections.bean(TransactionOrderView.class,
      transactionOrder.id,
      transactionOrder.dueDate,
      transactionOrder.relatedCompanyId,
      transactionOrder.stationId,
      transactionOrder.status,
      transactionOrder.type,
      transactionOrder.quantityCorrectionPolicy,
      transactionOrder.committedBy,
      transactionOrder.committedDate,
      transactionOrder.canceledBy,
      transactionOrder.canceledDate,
      transactionOrder.acceptedBy,
      transactionOrder.acceptedDate,
      transactionOrder.completedBy,
      transactionOrder.completedDate
    );
    query.select(select);
    query.from(transactionOrder);

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getCode())) {
      builder.and(
        transactionOrder.code.value.likeIgnoreCase(
          queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%")
        )
      );
    }

    if (filter.getItemId() != null) {
      builder.and(
        transactionOrder.id.in(
          JPAExpressions.select(transactionOrderItem.orderId)
            .from(transactionOrderItem)
            .where(transactionOrderItem.itemId.eq(filter.getItemId()))
        )
      );
    }
    if (filter.getRelatedCompanyId() != null) {
      builder.and(
        transactionOrder.relatedCompanyId.eq(filter.getRelatedCompanyId())
      );
    }
    if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
      builder.and(transactionOrder.status.in(filter.getStatuses()));
    }
    if (filter.getType() != null) {
      builder.and(transactionOrder.type.eq(filter.getType()));
    }
    if (filter.getCreateBy() != null) {
      builder.and(transactionOrder.createdBy.id.eq(filter.getCreateBy().getValue()));
    }
    if (filter.getStartCreatedDate() != null) {
      builder.and(transactionOrder.createdDate.goe(filter.getStartCreatedDate()));
    }
    if (filter.getEndCreatedDate() != null) {
      builder.and(transactionOrder.createdDate.loe(filter.getEndCreatedDate()));
    }
    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
