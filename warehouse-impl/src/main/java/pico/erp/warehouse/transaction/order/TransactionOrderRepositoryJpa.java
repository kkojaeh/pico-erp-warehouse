package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
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


@Repository
interface TransactionOrderEntityRepository extends
  CrudRepository<TransactionOrderEntity, TransactionOrderId> {

  @Query("SELECT o FROM TransactionOrder o WHERE o.status = :status AND o.dueDate < :fixedDate")
  Stream<TransactionOrderEntity> findAllDueDateBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate,
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
  public void deleteBy(@NotNull TransactionOrderId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull TransactionOrderId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<TransactionOrderAggregator> findAggregatorBy(
    TransactionOrderId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::aggregator);
  }

  @Override
  public Stream<TransactionOrder> findAllUncommittedAt(OffsetDateTime fixedDate) {
    return repository
      .findAllDueDateBeforeThan(fixedDate, TransactionOrderStatusKind.CREATED)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionOrder> findBy(@NotNull TransactionOrderId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionOrder transactionOrder) {
    val entity = repository.findOne(transactionOrder.getId());
    mapper.pass(mapper.jpa(transactionOrder), entity);
    repository.save(entity);
  }

}
