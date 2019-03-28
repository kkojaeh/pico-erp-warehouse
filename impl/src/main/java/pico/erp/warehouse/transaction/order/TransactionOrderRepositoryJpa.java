package pico.erp.warehouse.transaction.order;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.warehouse.transaction.request.TransactionRequestId;


@Repository
interface TransactionOrderEntityRepository extends
  CrudRepository<TransactionOrderEntity, TransactionOrderId> {

  @Query("SELECT COUNT(o) FROM TransactionOrder o WHERE o.createdDate >= :begin AND o.createdDate <= :end")
  long countCreatedBetween(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

  @Query("SELECT o FROM TransactionOrder o WHERE o.requestId = :requestId")
  Optional<TransactionOrderEntity> findBy(
    @Param("requestId") TransactionRequestId requestId);

  @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM TransactionOrder o WHERE o.code = :code")
  boolean exists(@Param("code") TransactionOrderCode code);

  @Query("SELECT o FROM TransactionOrder o WHERE o.status = :status AND o.dueDate < :fixedDate")
  Stream<TransactionOrderEntity> findAllUnacceptedAt(
    @Param("fixedDate") LocalDateTime fixedDate,
    @Param("status") TransactionOrderStatusKind status);

}

@Repository
@Transactional
public class TransactionOrderRepositoryJpa implements
  TransactionOrderRepository {

  @Autowired
  private TransactionOrderEntityRepository repository;

  @Autowired
  private TransactionOrderMapper mapper;

  @Override
  public TransactionOrder create(
    @NotNull TransactionOrder transactionOrder) {
    val entity = mapper.jpa(transactionOrder);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public long countCreatedBetween(LocalDateTime begin, LocalDateTime end) {
    return repository.countCreatedBetween(begin, end);
  }

  @Override
  public void deleteBy(@NotNull TransactionOrderId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull TransactionOrderId id) {
    return repository.existsById(id);
  }

  @Override
  public Optional<TransactionOrderAggregator> findAggregatorBy(
    TransactionOrderId id) {
    return repository.findById(id)
      .map(mapper::aggregator);
  }

  @Override
  public boolean exists(TransactionOrderCode code) {
    return repository.exists(code);
  }

  @Override
  public Stream<TransactionOrder> findAllUnacceptedAt(LocalDateTime fixedDate) {
    return repository
      .findAllUnacceptedAt(fixedDate, TransactionOrderStatusKind.COMMITTED)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionOrder> findBy(TransactionRequestId requestId) {
    return repository.findBy(requestId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionOrder> findBy(@NotNull TransactionOrderId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionOrder transactionOrder) {
    val entity = repository.findById(transactionOrder.getId()).get();
    mapper.pass(mapper.jpa(transactionOrder), entity);
    repository.save(entity);
  }

}
